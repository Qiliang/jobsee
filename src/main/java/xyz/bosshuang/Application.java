package xyz.bosshuang;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import xyz.bosshuang.entity.Company;
import xyz.bosshuang.entity.CompanyRepository;
import xyz.bosshuang.entity.Job;
import xyz.bosshuang.entity.JobRepository;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoq on 2018/7/22.
 */
@SpringBootApplication
@Slf4j
@EnableScheduling
public class Application {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobRepository jobRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @SneakyThrows
    private String doubleURLEncoder(String s) {
        return URLEncoder.encode(URLEncoder.encode(s, "utf-8"), "utf-8");
    }

    private List<String> urls(String key, final int pageSize) {

        String s1 = "https://search.51job.com/list/180200,000000,0000,00,9,99," + doubleURLEncoder(key) + ",2,";
        final String s2 = ".html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
        List<String> result = new ArrayList<>(pageSize);
        for (int i = 0; i < pageSize; i++) {
            result.add(s1 + (i + 1) + s2);
        }
        return result;
    }

    final int size = 40;
    final String[] keys = new String[]{
            "Java"
            , "Java开发工程师"
            , "Java高级开发工程师"
            , "Java开发实习生"
            , "Java初级开发工程师"
            , "Java中级开发工程师"
            , "Java资深开发工程师"
            , "Java架构师"
            , "Java开发经理"
            , "Java讲师"
    };

    @SneakyThrows
    @Scheduled(initialDelay = 1000, fixedRate = 10 * 60 * 60 * 1000)
    private void list51() {

        for (String key : keys) {
            for (String url : urls(key, size)) {
                Document doc = Jsoup.connect(url).get();
                log.info("{}", doc.title());
                Elements companyElems = doc.select("div.el>.t2>a");
                if (companyElems.size() == 0) break;
                for (Element companyElem : companyElems) {
                    String href = companyElem.attr("href");
                    //https://jobs.51job.com/all/co3851464.html
                    String siteId = DigestUtils.md5Hex(href);
                    if (companyRepository.existsBySiteId(siteId)) {
                        continue;
                    }
                    company51(href);
                }
                Elements jobElems = doc.select("div.el>.t1>span>a");
                for (Element jobElem : jobElems) {
                    String href = jobElem.attr("href");
                    System.out.println(jobElem.attr("href"));
                    //https://jobs.51job.com/wuhan-hsq/84189375.html?s=01&t=0
                    String siteId = href.substring(33, 41);
                    if (jobRepository.existsBySiteId(siteId)) {
                        continue;
                    }
                    content51(href);
                }
            }
        }
    }

    @SneakyThrows
    private void company51(String href) {
        try {
            String siteId = DigestUtils.md5Hex(href);
            if (companyRepository.existsBySiteId(siteId)) {
                return;
            }
            Document doc = Jsoup.connect(href).get();
            Element h1 = doc.selectFirst("h1");

            String ltype = doc.selectFirst(".ltype").text();
            String con_txt = doc.selectFirst(".con_txt").text();
            String fp = doc.selectFirst(".fp").ownText();
            String[] ltypes = ltype.replace("&nbsp;", "").split("\\|");
            Company company = Company.builder()
                    .siteId(siteId)
                    .name(h1.text())
                    .clazz(ltypes[0].trim())
                    .num(ltypes[1].trim())
                    .domain(ltypes[2].trim())
                    .description(con_txt)
                    .address(fp).source(href)
                    .build();

            companyRepository.save(company);
            log.info("{}:{}", h1.text(), h1.selectFirst("input").val());
        } catch (Exception e) {
            log.error("{}:{}", e.getMessage(), href);
        }
    }

    @SneakyThrows
    private void content51(String href) {
        try {
            String siteId = DigestUtils.md5Hex(href);
            if (jobRepository.existsBySiteId(siteId)) {
                return;
            }
            Document doc = Jsoup.connect(href).get();
            Element h1 = doc.selectFirst("h1");


            Element cname = doc.selectFirst(".cname>a");
            String companyHref = cname.attr("href");
            Elements sp4s = doc.select(".tCompany_main .t1 .sp4");
            List<String> tags = doc.select(".tCompany_main .t2>span").eachText();
            String fp = doc.selectFirst(".bmsg>.fp").ownText();
            Job job = Job.builder().title(h1.ownText())
                    .siteId(siteId)
                    .companySiteId(DigestUtils.md5Hex(companyHref))
                    .area(doc.selectFirst(".lname").text())
                    .salary(doc.selectFirst(".cn strong").text())
                    .experience(sp4s.get(0).text())
                    .education(sp4s.get(1).text())
                    .num(sp4s.get(2).text())
                    .publish(sp4s.get(2).text())
                    .tags(String.join(",", tags))
                    .description(doc.selectFirst("div.job_msg").html())
                    .address(fp).source(href).build();

            jobRepository.save(job);
        } catch (Exception e) {
            log.error("{}:{}", e.getMessage(), href);
        }
    }

}
