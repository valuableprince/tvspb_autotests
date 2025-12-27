package com.tvspb.tests.web;

import com.tvspb.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getDriver();
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}