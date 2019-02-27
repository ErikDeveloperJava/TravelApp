package net.travel.util;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Component
public class ImageUtil {

    @Value("${img.gen.path}")
    private String generalPath;

    private List<String> dirList;

    private List<String> imageFormatList;

    public ImageUtil() {
        dirList = Arrays
                .asList("hotel",
                        "hotel_room",
                        "place",
                        "user");
        imageFormatList = Arrays
                .asList(
                        "image/jpeg",
                        "image/png");
    }

    @PostConstruct
    public void init(){
        File file = new File(generalPath);
        if(!file.exists()){
            file.mkdirs();
        }
        for (String dir : dirList) {
            file = new File(generalPath,dir);
            if(!file.exists()){
                file.mkdir();
            }
        }
    }

    public boolean isValidFormat(String actualFormat){
        for (String imageFormat : imageFormatList) {
            if(imageFormat.equals(actualFormat)){
                return true;
            }
        }
        return false;
    }

    public void createDir(String dir){
        File file = new File(generalPath,dir);
        if(!file.exists()){
            file.mkdir();
        }
    }

    @SneakyThrows
    public void save(String image, MultipartFile multipartFile){
        File file = new File(generalPath,image);
        multipartFile.transferTo(file);
    }

    public void delete(String fileName){
        File file = new File(generalPath,fileName);
        if(file.exists()){
            delete(file);
        }
    }

    private void delete(File file) {
        if(file.isDirectory()){
            for (File f : file.listFiles()) {
                delete(f);
            }
        }
        file.delete();
    }
}