package controller.command;

import model.entity.Movie;
import model.entity.User;
import model.service.MovieService;
import model.util.Languages;
import model.util.LogGen;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.util.LogMsg.*;

@MultipartConfig
public class AddMovie implements Command{
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";
    private final String LOCAL_RB_BASE_NAME = "lang";
    private final String MESSAGE = "msg";
    private final String MOVIE_NAME_ENG_PARAM = "engName";
    private final String MOVIE_NAME_UKR_PARAM = "ukrName";

    private static Logger log = LogGen.getInstance();
    private static final int MEMORY_THRESHOLD = 1024 * 1024;
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 5;
    private static final long MAX_REQUEST_SIZE = 1024 * 1024 * 5 * 5;
    private static final String UPLOAD_DIRECTORY = "picture\\filmPic";

    private String movieNameEng;
    private String movieNameUkr;
    private String pictureName;

    private MovieService movieService;

    public AddMovie(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User curUser = (User) request.getSession().getAttribute(SESSION_USER);
        Optional<Object> role = Optional.ofNullable(curUser.getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));
        ResourceBundle rsBundle = ResourceBundle.getBundle(LOCAL_RB_BASE_NAME, locale);

        String outUrlOK = role.map(o -> "forward:/WEB-INF/" + o.toString() + "/index.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString()))
                .orElse("forward:/login");
        String outUrlInvalid = "redirect:/WEB-INF/util/operation_fail.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());

        //movieNameEng = (String) request.getAttribute(MOVIE_NAME_ENG_PARAM);
        //movieNameUkr = request.getParameter(MOVIE_NAME_UKR_PARAM);
        //System.out.println("movie eng and ukr: " + movieNameEng + ", "+ movieNameUkr);
        try {
            processPictureFromReq(request);
        } catch (Exception e) {
            log.error(CANT_DOWNLOAD_PICTURE, e);
        }


        if (invalidData(movieNameEng, movieNameUkr)) {
            request.setAttribute(MESSAGE, rsBundle.getString("wrong.data"));
            log.info(INVALID_DATA_MOVIE_CREATION + " Mov name eng: " + movieNameEng + " Mov name urk: " + movieNameUkr);
            return outUrlInvalid;
        }
        Movie movieToDB = setPictureForMovie(pictureName);
        movieService.createMovie(movieToDB, movieNameEng, movieNameUkr);
        log.info(MOVIE_CREATED_SUCCESSFULLY + " Mov name eng: " + movieNameEng + " Mov name urk: " + movieNameUkr);
        return outUrlOK;
    }

    private void processPictureFromReq(HttpServletRequest request) throws Exception {
       /* Part filePart = request.getPart("pic"); // Retrieves <input type="file" name="file">
        System.out.println("filePart::::: " + filePart);
        pictureName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                //getSubmittedFileName(filePart); // MSIE fix.

        System.out.println("picture name: " + pictureName);
        File uploads = new File("C:\\java\\javaTools\\git\\Projects\\cinema-finalProject\\cinema(finalProject)\\src\\main\\webapp\\picture\\filmPic");

        File file = new File(uploads, movieNameEng);

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
        }*/
        // ... (do your job here)
        if (ServletFileUpload.isMultipartContent(request)) {

            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(MAX_FILE_SIZE);
            upload.setSizeMax(MAX_REQUEST_SIZE);
            String uploadPath = request.getServletContext().getRealPath("")
                    /*+ File.separator*/ + UPLOAD_DIRECTORY;
           File uploadDir = new File(uploadPath);
            System.out.println("upload PATH::: " + uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        pictureName = fileName;
                    } else {
                        if (item.getFieldName().equals(MOVIE_NAME_ENG_PARAM)) {
                            movieNameEng = new String(item.getString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        } else if (item.getFieldName().equals(MOVIE_NAME_UKR_PARAM)) {
                            movieNameUkr = new String(item.getString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        }
                    }
                }
            }
        }
    }
    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }


    private boolean invalidData(String movieNameEng, String movieNameUkr) {
        return !Optional.ofNullable(movieNameEng).isPresent() ||
                !Optional.ofNullable(movieNameUkr).isPresent();
    }

    private Movie setPictureForMovie(String picUrl) {
        Movie movie = new Movie();
        movie.setPicUrl(picUrl);
        return movie;
    }
}
