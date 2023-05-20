package in.alqaholic.springSpa;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SPAController {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    private String respondWithIndex(Model model)
            throws StreamReadException, DatabindException, IOException {

        if (activeProfile.contains("production")) {
            ObjectMapper mapper = new ObjectMapper();

            var manifestInputStream = new ClassPathResource("manifest.json").getInputStream();

            var manifest = mapper.readTree(manifestInputStream);

            var entrypoint = manifest.get("main.tsx");

            var cssFiles = new ArrayList<String>();
            for (var css : entrypoint.get("css")) {
                cssFiles.add(css.asText());
            }

            var entrypointJS = entrypoint.get("file").asText();

            model.addAttribute("cssFiles", cssFiles);
            model.addAttribute("entrypoint", entrypointJS);
        }

        return "index.html";
    }

    @RequestMapping("/{path:^(?!api|static).*}/**")
    public String indexPattern(final Model model)
            throws StreamReadException, DatabindException, IOException {
        return respondWithIndex(model);
    }

}