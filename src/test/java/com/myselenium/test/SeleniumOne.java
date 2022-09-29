package com.myselenium.test;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SeleniumOne {
    WebDriver  driver;
    String url="http://www.baidu.com";
    //设置搜索关键词，关键字驱动
    @DataProvider(name="searchWords")
    Object[][] searchWords()
    {

        return new Object[][]{{"沉思录"}};
    }

    //初始化driver
    @BeforeClass
    public void beforeClass()
    {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver=new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1000,TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(url);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //case1：关键字搜索，结果验证
    @Test(dataProvider="searchWords")
    public void  test01(String searchWord1) throws InterruptedException
    {
        //模拟将鼠标放在设置上
        Actions action=new Actions(driver);
        action.moveToElement(driver.findElement(By.id("s-usersetting-top"))).perform();
        driver.findElement(By.xpath("//span[contains(text(),'搜索设置')]")).click();//搜索设置
        WebElement ele=driver.findElement(By.id("nr_2"));
        ele.click();
        Thread.sleep(3000);

        driver.findElement(By.xpath("//a[contains(text(),'保存设置')]")).click();
        Thread.sleep(2000);

        driver.switchTo().alert().accept();// alert弹窗提示点确定
        driver.findElement(By.id("kw")).sendKeys(searchWord1);
        driver.findElement(By.id("su")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        List<WebElement> eles=driver.findElements(By.className("c-title"));
        WebElement daa=driver.findElement(By.cssSelector("[class='t c-gap-bottom-small']"));
        eles.add(daa);
        Assert.assertEquals(eles.size(),20);

    }

    //case2：验证搜索结果含有广告，且打印广告条数
    @Test
    public void  test02() throws InterruptedException
    {
        Thread.sleep(1000);
        //查找广告
        List<WebElement> eleGG=driver.findElements(By.cssSelector("[class='ec-tuiguang ecfc-tuiguang _2awtgst']"));
        Assert.assertNotNull(eleGG);
        System.out.println("共出现"+eleGG.size()+"条广告");

    }
    //case3：使用“搜索工具”按钮，设置搜索时间为一天内，检查搜索结果都是一天内的。
    @Test
    public void  test03() throws InterruptedException
    {
        driver.findElement(By.xpath("//*[@id=\"tsn_inner\"]/div[2]/div/i")).click();  //搜索工具
        Thread.sleep(1000);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        driver.findElement(By.id("timeRlt")).click();
        driver.findElement(By.xpath("//li[contains(text(),'一天内')]")).click();
        Thread.sleep(3000);

        //结果判断
        List<WebElement> eles=driver.findElements(By.cssSelector("[class='c-color-gray2']"));
        boolean result=true;
        for(int i=0;i<eles.size();i++)
        {
            String time=eles.get(i).getText();
            if(!(time.contains("小时") || time.contains("分钟")))
            {
                result=false;
                break;
            }
        }

        Assert.assertTrue(result,"查询结果并不都是一天之内的");
    }


    @AfterClass
    public void afterClass()
    {
        driver.close();

    }

}