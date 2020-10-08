package tech.yiyehu.locust;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tech.yiyehu.locust.config.ApplicationConfig;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LocustApplication {

    public static void main(String[] args) {
        try{
            throw new FileNotFoundException();
        }
        catch (FileNotFoundException e){

        }catch (Exception e){

        }
    }
}
