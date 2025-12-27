package com.tvspb.pages.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//img[contains(@src, 'logo')]")
    private WebElement logo;

    @FindBy(xpath = "//img[contains(@src, 'logo-15.svg')]")
    private WebElement logoSvg;

    @FindBy(css = "input[type='search'], input[name*='search'], input[placeholder*='поиск'], input[placeholder*='Поиск']")
    private WebElement searchInput;

    @FindBy(css = "button[type='submit'], button[class*='search'], input[type='submit'], button:contains('Найти')")
    private WebElement searchButton;

    @FindBy(xpath = "//a[contains(@href, 'news') or contains(@href, 'новост') or contains(text(), 'Новости') or contains(text(), 'NEWS')]")
    private WebElement newsLink;

    @FindBy(xpath = "//a[contains(@href, 'program') or contains(@href, 'программ') or contains(text(), 'Программы') or contains(text(), 'Передачи')]")
    private WebElement programLink;

    @FindBy(xpath = "//a[contains(@href, 'contact') or contains(@href, 'контакт') or contains(text(), 'Контакты') or contains(text(), 'CONTACT')]")
    private WebElement contactsLink;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get("https://tvspb.ru/");
        waitForPageLoad();
    }

    private void waitForPageLoad() {
        wait.until(webDriver ->
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isLogoDisplayed() {
        try {
            if (logoSvg != null && logoSvg.isDisplayed()) {
                System.out.println("SVG логотип найден и отображается");
                return true;
            }

            if (logo != null && logo.isDisplayed()) {
                System.out.println("Логотип найден и отображается");
                return true;
            }

            WebElement foundLogo = driver.findElement(
                org.openqa.selenium.By.xpath("//img[contains(@src, 'logo-15.svg')]")
            );

            if (foundLogo != null && foundLogo.isDisplayed()) {
                System.out.println("Логотип найден через прямой поиск");
                return true;
            }

            return false;

        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("Логотип не найден: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Ошибка при проверке логотипа: " + e.getMessage());
            return false;
        }
    }

    private WebElement findElementWithWait(org.openqa.selenium.By by, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public void searchFor(String query) {
        try {
            System.out.println("Поиск текста: " + query);


            WebElement searchField = null;

            try {
                searchField = findElementWithWait(
                    org.openqa.selenium.By.cssSelector("input[placeholder*='поиск'], input[placeholder*='Поиск']"),
                    5
                );
            } catch (Exception e1) {
                try {
                    // Вариант 2: по name
                    searchField = findElementWithWait(
                        org.openqa.selenium.By.cssSelector("input[name*='search'], input[name*='s'], input[name='q']"),
                        5
                    );
                } catch (Exception e2) {
                    try {
                        // Вариант 3: по классу
                        searchField = findElementWithWait(
                            org.openqa.selenium.By.cssSelector("input.search-input, input.search-field"),
                            5
                        );
                    } catch (Exception e3) {
                        System.out.println("Поле поиска не найдено");
                        return;
                    }
                }
            }

            if (searchField != null) {
                searchField.clear();
                searchField.sendKeys(query);
                System.out.println("Текст введен в поле поиска");

                try {
                    WebElement searchBtn = driver.findElement(
                        org.openqa.selenium.By.cssSelector("button[type='submit'], input[type='submit'], button.search-button")
                    );
                    searchBtn.click();
                    System.out.println("Кнопка поиска нажата");
                } catch (Exception e) {
                    searchField.sendKeys(org.openqa.selenium.Keys.ENTER);
                    System.out.println("Использован Enter для поиска");
                }

                Thread.sleep(3000);
            }

        } catch (Exception e) {
            System.out.println("Ошибка при поиске: " + e.getMessage());
        }
    }

    public void clickNewsLink() {
        clickElementByXpath("//a[contains(@href, 'news') or contains(text(), 'Новости')]", "Новости");
    }

    public void clickProgramLink() {
        clickElementByXpath("//a[contains(@href, 'program') or contains(text(), 'Программ')]", "Программы");
    }

    public void clickContactsLink() {
        clickElementByXpath("//a[contains(@href, 'contact') or contains(text(), 'Контакт')]", "Контакты");
    }

    private void clickElementByXpath(String xpath, String elementName) {
        try {
            WebElement element = findElementWithWait(org.openqa.selenium.By.xpath(xpath), 10);
            if (element != null && element.isDisplayed() && element.isEnabled()) {
                element.click();
                System.out.println("Кликнули на элемент: " + elementName);

                Thread.sleep(3000);
                waitForPageLoad();
            }
        } catch (Exception e) {
            System.out.println("Не удалось кликнуть на " + elementName + ": " + e.getMessage());

            try {
                java.util.List<WebElement> allLinks = driver.findElements(
                    org.openqa.selenium.By.tagName("a")
                );

                for (WebElement link : allLinks) {
                    String href = link.getAttribute("href");
                    String text = link.getText();

                    if (href != null && (href.contains(elementName.toLowerCase()) ||
                        (text != null && text.toLowerCase().contains(elementName.toLowerCase())))) {
                        link.click();
                        System.out.println("Кликнули на альтернативную ссылку: " + text);
                        Thread.sleep(3000);
                        break;
                    }
                }
            } catch (Exception ex) {
                System.out.println("Альтернативный поиск также не удался");
            }
        }
    }

    // Вспомогательные методы для отладки
    public void printPageInfo() {
        System.out.println("=== ИНФОРМАЦИЯ О СТРАНИЦЕ ===");
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Заголовок: " + driver.getTitle());
        System.out.println("Размер страницы: " + driver.getPageSource().length() + " символов");
        System.out.println("===========================");
    }

    public void printAllMenuLinks() {
        System.out.println("=== МЕНЮ САЙТА ===");
        try {
            java.util.List<WebElement> menuLinks = driver.findElements(
                org.openqa.selenium.By.cssSelector("nav a, .menu a, .navbar a, header a")
            );

            for (int i = 0; i < menuLinks.size(); i++) {
                WebElement link = menuLinks.get(i);
                String text = link.getText().trim();
                String href = link.getAttribute("href");

                if (!text.isEmpty()) {
                    System.out.println((i+1) + ". " + text + " -> " + href);
                }
            }
        } catch (Exception e) {
            System.out.println("Не удалось получить меню: " + e.getMessage());
        }
        System.out.println("=================");
    }

    // Геттеры
    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}