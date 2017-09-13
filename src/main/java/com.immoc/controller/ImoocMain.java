package com.immoc.controller;

import com.immoc.constant.CommonConstant;
import com.immoc.service.DownloadFile;
import com.immoc.service.GetInput;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

public class ImoocMain {

    private static int curruntCount;

    public static void main(String[] args) throws Exception {
        while (true) {
            curruntCount = 0;
            int classNo = GetInput.getInputClassNo();
            Document doc = Jsoup.connect(CommonConstant.IMOOC_VIDEO_URL + "learn/" + classNo).get();
            String title = doc.getElementsByTag("h2").html();
            Elements videos = doc.select(".video a");
            if ((title.equals("")) && (videos.size() == 0)) {
                System.out.println("抱歉，没有该课程!");
            } else {
                int count = 0;
                for (Element video : videos) {
                    String[] videoNos = video.attr("href").split("/");
                    if (videoNos.length >= 2 && videoNos[1].equals("video")) {
                        count++;
                    }
                }

                System.out.println("要下载的课程标题为【" + title + "】");
                System.out.println("本次要下载的视频课程有 " + count + " 节");
                int videoDef = 0;   //这里默认下载超清的
                String savePath = "./download/" + title + "/";
                File file = new File(savePath);
                file.mkdirs();
                System.out.println("准备开始下载，请耐心等待…");

                for (Element video : videos) {
                    String[] videoNos = video.attr("href").split("/");

                    //只下载视频
                    if (videoNos.length > 1 && videoNos[1].equals("video")) {
                        video.select("button").remove();
                        String videoName = video.text().trim();
                        videoName = videoName.substring(0, videoName.length() - 7).trim();
                        String videoNo = videoNos[2];

                        Document jsonDoc = Jsoup.connect(CommonConstant.IMOOC_VIDEO_URL + "course/ajaxmediainfo/?mid=" + videoNo + "&mode=flash").get();
                        String jsonData = jsonDoc.text();

                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray mpath = jsonObject.optJSONObject("data").optJSONObject("result").optJSONArray("mpath");
                        String downloadPath = mpath.getString(videoDef).trim();
                        DownloadFile.downLoadFromUrl(downloadPath, videoName + ".mp4", savePath);
                        curruntCount += 1;
                        System.out.println("【" + curruntCount + "】：\t" + videoName + "\t下载成功！");
                    }
                }

                System.out.println("恭喜！【" + title + "】课程的所有视频已经下载完成！！！下载的文件在该程序所在目录下的download文件夹中。");
            }
        }
    }
}
