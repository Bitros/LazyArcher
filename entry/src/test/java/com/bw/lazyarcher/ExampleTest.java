package com.bw.lazyarcher;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.Random;

public class ExampleTest {
    @Test
    public void onStart() {
        SecureRandom random = new SecureRandom();
        for (int i =0;i < 20;i++){
        System.out.println(random.nextInt(10));
        }
    }

}
