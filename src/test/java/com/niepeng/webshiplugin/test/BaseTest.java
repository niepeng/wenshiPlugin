package com.niepeng.webshiplugin.test;
/**
 * Created by lsb on 17/8/2.
 */


import com.niepeng.xue.wenshiplugin.App;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/2
 */

@SpringBootTest(classes = App.class)
@ComponentScan("com.niepeng")
@EnableConfigurationProperties
@PropertySource("classpath:application.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class BaseTest {

  @BeforeClass
  public static void init() {
//    System.setProperty("service.tag", "local");
//    //you can do something here
//    System.setProperty("server.port", "8090");
  }

}