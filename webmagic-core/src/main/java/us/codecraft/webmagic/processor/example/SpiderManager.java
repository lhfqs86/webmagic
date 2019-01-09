package us.codecraft.webmagic.processor.example;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.SimplePageProcessor;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpiderManager {

    private Timer timer = new Timer();

    public void start() {
        timer.schedule(new Task(), 1, 600000);
    }

    public static void main(String[] args) {
        new SpiderManager().start();
    }
}


 class Site{
    public String domain;
    public PageProcessor pageProcessor;

    public Site(String domain, PageProcessor pageProcessor){
        this.domain = domain;
        this.pageProcessor = pageProcessor;
    }
}


class SpiderTask implements  Runnable {
    private Site site;

    public SpiderTask(Site site){
        this.site = site;
    }

    public void run(){
        Spider.create(site.pageProcessor).addUrl(site.domain).run();
    }
}

class Task extends TimerTask {
    private final ArrayList<Site> sites = new ArrayList<Site>();

    private void setupPageProcessors(){
        sites.clear();
        sites.add(new Site("https://www.zhihu.com/explore", new ZhihuPageProcessor()));
        //sites.add(new Site("https://github.com/code4craft", new GithubRepoPageProcessor()));
    }

    public Task(){
        setupPageProcessors();
    }

    public void run(){
        ExecutorService exes = Executors.newFixedThreadPool(1);
        for(Site s : sites)
            exes.execute(new SpiderTask(s));
    }
}
