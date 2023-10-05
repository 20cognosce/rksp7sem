package ru.mirea.prac3.task3;

import io.reactivex.rxjava3.core.Observable;

import java.util.List;
import java.util.Random;

public class Task3Manager implements Runnable {

    private UserFriend[] userFriends;

    @Override
    public void run() {
        this.userFriends = generateRandomUserFriends();

        List<Integer> randomUserId = new Random()
                .ints(10, 1, 101)
                .boxed().toList();

        randomUserId.forEach(System.out::println);
        System.out.println("========================");

        Observable.fromIterable(randomUserId)
                .flatMap(id -> getFriends(id).toList().toObservable())
                .flatMap(Observable::fromIterable)
                .subscribe(userFriend -> System.out.println("UserId: " + userFriend.userId() + ", FriendId: " + userFriend.friendId()));
    }

    private Observable<UserFriend> getFriends(int userId) {
        Observable<UserFriend> userFriendObservable = Observable.fromArray(userFriends);
        return userFriendObservable.filter(userFriend -> userFriend.userId() == userId);
    }

    private static UserFriend[] generateRandomUserFriends() {
        UserFriend[] userFriends = new UserFriend[100];
        for (int i = 0; i < 100; i++) {
            int userId = (int) (Math.random() * 100);
            int friendId = (int) (Math.random() * 100);
            userFriends[i] = new UserFriend(userId, friendId);
        }
        return userFriends;
    }
}
