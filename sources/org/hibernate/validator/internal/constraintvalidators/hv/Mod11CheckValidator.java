package org.hibernate.validator.internal.constraintvalidators.hv;

import java.util.Collections;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraints.Mod11Check;
import org.hibernate.validator.internal.util.ModUtil;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/hv/Mod11CheckValidator.class */
public class Mod11CheckValidator extends ModCheckBase implements ConstraintValidator<Mod11Check, CharSequence> {
    private static final Log log = LoggerFactory.make();
    private boolean reverseOrder;
    private char treatCheck10As;
    private char treatCheck11As;
    private int threshold;

    @Override // javax.validation.ConstraintValidator
    public /* bridge */ /* synthetic */ boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        return super.isValid(charSequence, constraintValidatorContext);
    }

    @Override // javax.validation.ConstraintValidator
    public void initialize(Mod11Check constraintAnnotation) {
        initialize(constraintAnnotation.startIndex(), constraintAnnotation.endIndex(), constraintAnnotation.checkDigitIndex(), constraintAnnotation.ignoreNonDigitCharacters(), constraintAnnotation.threshold(), constraintAnnotation.treatCheck10As(), constraintAnnotation.treatCheck11As(), constraintAnnotation.processingDirection());
    }

    public void initialize(int startIndex, int endIndex, int checkDigitIndex, boolean ignoreNonDigitCharacters, int threshold, char treatCheck10As, char treatCheck11As, Mod11Check.ProcessingDirection direction) {
        super.initialize(startIndex, endIndex, checkDigitIndex, ignoreNonDigitCharacters);
        this.threshold = threshold;
        this.reverseOrder = direction == Mod11Check.ProcessingDirection.LEFT_TO_RIGHT;
        this.treatCheck10As = treatCheck10As;
        this.treatCheck11As = treatCheck11As;
        if (!Character.isLetterOrDigit(this.treatCheck10As)) {
            throw log.getTreatCheckAsIsNotADigitNorALetterException(this.treatCheck10As);
        }
        if (!Character.isLetterOrDigit(this.treatCheck11As)) {
            throw log.getTreatCheckAsIsNotADigitNorALetterException(this.treatCheck11As);
        }
    }

    @Override // org.hibernate.validator.internal.constraintvalidators.hv.ModCheckBase
    public boolean isCheckDigitValid(List<Integer> digits, char checkDigit) {
        if (this.reverseOrder) {
            Collections.reverse(digits);
        }
        int modResult = ModUtil.calculateMod11Check(digits, this.threshold);
        switch (modResult) {
            case 10:
                if (checkDigit == this.treatCheck10As) {
                }
                break;
            case 11:
                if (checkDigit == this.treatCheck11As) {
                }
                break;
            default:
                if (!Character.isDigit(checkDigit) || modResult != extractDigit(checkDigit)) {
                }
                break;
        }
        return false;
    }
}
