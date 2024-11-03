package org.sergeydevjava.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.sergeydevjava.repository.LinkInfoRepository;

import java.time.LocalDateTime;


@RequiredArgsConstructor
public class LinkValidator implements ConstraintValidator<ValidLink, String> {

    private final LinkInfoRepository linkInfoRepository;

    @Override
    public boolean isValid(String shortLink, ConstraintValidatorContext constraintValidatorContext) {
        return
                linkInfoRepository.findByShortLink(shortLink)
                        .filter(
                                linkInfo -> linkInfo.getActive()
                                        && LocalDateTime.now().isBefore(linkInfo.getEndTime()))
                        .isPresent();

    }
}
