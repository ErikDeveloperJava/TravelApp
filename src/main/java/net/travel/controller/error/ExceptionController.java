package net.travel.controller.error;

import net.travel.util.TemplateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(Exception.class)
    public String exceptionCatch(Exception e){
        LOGGER.info(e.getMessage(),e);
        return TemplateUtil.ERROR_500;
    }
}