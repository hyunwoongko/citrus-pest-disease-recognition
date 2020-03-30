package com.hyunwoong.pestsaver.util.data;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hyunwoong.pestsaver.core.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 2019-11-18 오후 12:16
 * @homepage : https://github.com/gusdnd852
 */
public final class RxFirebase {
    // RxJava Style Wrapper Class of FirebaseDatabase & Auth

    private static Accessor accessor = new Accessor();
    private static AuthAccessor authManager = new AuthAccessor();

    public static Accessor from(String root) {
        return accessor.from(root);
    }

    public static Accessor from() {
        return accessor.from();
    }

    public static String uid() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static AuthProcessor signIn() {
        return authManager.signIn();
    }

    public static AuthProcessor signUp() {
        return authManager.signUp();
    }

    public static class Accessor {
        private DatabaseReference reference;

        Accessor() {

        }

        public Accessor from(String root) {
            this.reference = FirebaseDatabase.getInstance().getReference(root);
            return this;
        }

        public Accessor from() {
            this.reference = FirebaseDatabase.getInstance().getReference();
            return this;
        }

        public Accessor child(String path) {
            this.reference = reference.child(path);
            return this;
        }

        public <T> Publisher<T> access(Class<T> clazz) {
            return new Publisher<>(clazz, reference);
        }
    }

    public static class Publisher<T> {
        private Class<T> clazz;
        private DatabaseReference reference;

        Publisher(Class<T> clazz, DatabaseReference reference) {
            this.clazz = clazz;
            this.reference = reference;
        }

        public <R> Mapper<T, R> map(Function<T, R> function) {
            return new Mapper<>(reference, this, function);
        }

        public Next<T> next(Consumer<T> function) {
            return new Next<>(reference, this, function);
        }

        public void upload(T obj) {
            reference.setValue(obj);
        }

        public void subscribe(Consumer<? super T> subscriber) {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subscriber.accept(dataSnapshot.getValue(clazz));
                }

                @Override public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        public void subscribe() {
            this.subscribe(t -> {

            });
        }
    }

    public static class Mapper<T, R> extends Publisher<R> {

        private Publisher<T> processor;
        private Function<T, R> function;

        Mapper(DatabaseReference reference, Publisher<T> processor, Function<T, R> function) {
            super(null, reference);
            this.processor = processor;
            this.function = function;
        }

        @Override public void subscribe(Consumer<? super R> subscriber) {
            processor.subscribe(t ->
                    subscriber.accept(function.apply(t)));
        }
    }

    public static class Next<T> extends Publisher<T> {

        private Publisher<T> processor;
        private Consumer<T> function;

        Next(DatabaseReference reference, Publisher<T> processor, Consumer<T> function) {
            super(null, reference);
            this.processor = processor;
            this.function = function;
        }

        @Override public void subscribe(Consumer<? super T> subscriber) {
            processor.subscribe(t -> {
                function.accept(t);
                subscriber.accept(t);
            });
        }
    }

    public static class AuthAccessor {
        private Mode mode;

        public AuthProcessor signIn() {
            this.mode = Mode.signIn;
            return new AuthProcessor(mode);
        }

        public AuthProcessor signUp() {
            this.mode = Mode.signUp;
            return new AuthProcessor(mode);
        }

        private enum Mode {
            signIn, signUp
        }

    }

    public static class AuthProcessor {
        private Task<AuthResult> currentTask;
        private List<Consumer<User>> success = new ArrayList<>();
        private List<Consumer<User>> failure = new ArrayList<>();
        private AuthAccessor.Mode mode;

        public AuthProcessor(AuthAccessor.Mode mode) {
            this.mode = mode;
        }

        public AuthProcessor success(Consumer<User> func) {
            success.add(func);
            return this;
        }

        public AuthProcessor fail(Consumer<User> func) {
            failure.add(func);
            return this;
        }

        public void subscribe(User user) {
            if (mode == AuthAccessor.Mode.signIn) currentTask =
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getId(), user.getPw());

            else if (mode == AuthAccessor.Mode.signUp) currentTask =
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getId(), user.getPw());

            currentTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) for (Consumer<User> func : success) func.accept(user);
                else for (Consumer<User> func : failure) func.accept(user);
                currentTask = null;
            });
        }
    }
}