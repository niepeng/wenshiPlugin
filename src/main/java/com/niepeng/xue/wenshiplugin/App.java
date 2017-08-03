package com.niepeng.xue.wenshiplugin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/2
 */
@SpringBootApplication
@EnableScheduling
public class App {

  public static void main(String args []) {
    SpringApplication.run(App.class, args);
  }

}