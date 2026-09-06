package com.papapa.stack.pr_test.task;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Locator;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TaskCompletionE2ETest {

    private static final String BASE_URL =
            System.getProperty("e2e.base-url", "http://127.0.0.1:8080");

    @Test
    void userCanCompleteTaskFromTheTaskList() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(
                     new BrowserType.LaunchOptions().setHeadless(true))) {
            Page page = browser.newPage();
            String title = "E2E task " + UUID.randomUUID();

            page.navigate(BASE_URL + "/tasks/new");
            page.locator("input[name='title']").fill(title);
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Save")).click();

            Locator task = page.locator("li")
                    .filter(new Locator.FilterOptions().setHasText(title));
            assertThat(task).containsText("OPEN");
            assertThat(task.getByRole(AriaRole.BUTTON,
                    new Locator.GetByRoleOptions().setName("Complete"))).isVisible();

            task.getByRole(AriaRole.BUTTON,
                    new Locator.GetByRoleOptions().setName("Complete")).click();

            assertThat(task).containsText("DONE");
            assertThat(task.getByRole(AriaRole.BUTTON,
                    new Locator.GetByRoleOptions().setName("Complete"))).isHidden();
        }
    }
}
