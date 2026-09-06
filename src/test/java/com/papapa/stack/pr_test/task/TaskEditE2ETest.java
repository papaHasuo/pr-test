package com.papapa.stack.pr_test.task;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TaskEditE2ETest {

    private static final String BASE_URL =
            System.getProperty("e2e.base-url", "http://127.0.0.1:8080");

    @Test
    void userCanEditTaskFromTheTaskList() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(
                     new BrowserType.LaunchOptions().setHeadless(true))) {
            Page page = browser.newPage();
            String originalTitle = "E2E task " + UUID.randomUUID();
            String updatedTitle = originalTitle + " updated";

            page.navigate(BASE_URL + "/tasks/new");
            page.locator("input[name='title']").fill(originalTitle);
            page.locator("textarea[name='description']").fill("Original description");
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Save")).click();

            Locator task = page.locator("li")
                    .filter(new Locator.FilterOptions().setHasText(originalTitle));
            task.getByRole(AriaRole.LINK,
                    new Locator.GetByRoleOptions().setName("Edit")).click();

            assertThat(page.locator("input[name='title']")).hasValue(originalTitle);
            assertThat(page.locator("textarea[name='description']"))
                    .hasValue("Original description");

            page.locator("input[name='title']").fill(updatedTitle);
            page.locator("textarea[name='description']").fill("Updated description");
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Update")).click();

            Locator updatedTask = page.locator("li")
                    .filter(new Locator.FilterOptions().setHasText(updatedTitle));
            assertThat(updatedTask).containsText("Updated description");
        }
    }

    @Test
    void invalidTitle_shouldBeShownOnTheEditForm() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(
                     new BrowserType.LaunchOptions().setHeadless(true))) {
            Page page = browser.newPage();
            String title = "E2E validation task " + UUID.randomUUID();

            page.navigate(BASE_URL + "/tasks/new");
            page.locator("input[name='title']").fill(title);
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Save")).click();

            page.locator("li")
                    .filter(new Locator.FilterOptions().setHasText(title))
                    .getByRole(AriaRole.LINK,
                            new Locator.GetByRoleOptions().setName("Edit")).click();
            page.locator("input[name='title']").fill(" ");
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Update")).click();

            assertThat(page.getByText("Title must not be blank")).isVisible();
        }
    }
}
