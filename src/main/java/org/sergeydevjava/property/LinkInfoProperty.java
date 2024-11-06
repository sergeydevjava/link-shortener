package org.sergeydevjava.property;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties("link-shortener")
public class LinkInfoProperty {

    @Min(value = 8, message = "Длинна короткой ссылки не должна быть меньше 8 символов")
    private Integer shortLinkLength;
}
