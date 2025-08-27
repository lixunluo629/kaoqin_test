package org.springframework.boot.autoconfigure.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/logging/ConditionEvaluationReportMessage.class */
public class ConditionEvaluationReportMessage {
    private StringBuilder message;

    public ConditionEvaluationReportMessage(ConditionEvaluationReport report) {
        this.message = getLogMessage(report);
    }

    private StringBuilder getLogMessage(ConditionEvaluationReport report) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("%n%n%n", new Object[0]));
        message.append(String.format("=========================%n", new Object[0]));
        message.append(String.format("AUTO-CONFIGURATION REPORT%n", new Object[0]));
        message.append(String.format("=========================%n%n%n", new Object[0]));
        message.append(String.format("Positive matches:%n", new Object[0]));
        message.append(String.format("-----------------%n", new Object[0]));
        Map<String, ConditionEvaluationReport.ConditionAndOutcomes> shortOutcomes = orderByName(report.getConditionAndOutcomesBySource());
        for (Map.Entry<String, ConditionEvaluationReport.ConditionAndOutcomes> entry : shortOutcomes.entrySet()) {
            if (entry.getValue().isFullMatch()) {
                addMatchLogMessage(message, entry.getKey(), entry.getValue());
            }
        }
        message.append(String.format("%n%n", new Object[0]));
        message.append(String.format("Negative matches:%n", new Object[0]));
        message.append(String.format("-----------------%n", new Object[0]));
        for (Map.Entry<String, ConditionEvaluationReport.ConditionAndOutcomes> entry2 : shortOutcomes.entrySet()) {
            if (!entry2.getValue().isFullMatch()) {
                addNonMatchLogMessage(message, entry2.getKey(), entry2.getValue());
            }
        }
        message.append(String.format("%n%n", new Object[0]));
        message.append(String.format("Exclusions:%n", new Object[0]));
        message.append(String.format("-----------%n", new Object[0]));
        if (report.getExclusions().isEmpty()) {
            message.append(String.format("%n    None%n", new Object[0]));
        } else {
            for (String exclusion : report.getExclusions()) {
                message.append(String.format("%n    %s%n", exclusion));
            }
        }
        message.append(String.format("%n%n", new Object[0]));
        message.append(String.format("Unconditional classes:%n", new Object[0]));
        message.append(String.format("----------------------%n", new Object[0]));
        if (report.getUnconditionalClasses().isEmpty()) {
            message.append(String.format("%n    None%n", new Object[0]));
        } else {
            for (String unconditionalClass : report.getUnconditionalClasses()) {
                message.append(String.format("%n    %s%n", unconditionalClass));
            }
        }
        message.append(String.format("%n%n", new Object[0]));
        return message;
    }

    private Map<String, ConditionEvaluationReport.ConditionAndOutcomes> orderByName(Map<String, ConditionEvaluationReport.ConditionAndOutcomes> outcomes) {
        Map<String, ConditionEvaluationReport.ConditionAndOutcomes> result = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        Map<String, String> classNames = new HashMap<>();
        for (String name : outcomes.keySet()) {
            String shortName = ClassUtils.getShortName(name);
            names.add(shortName);
            classNames.put(shortName, name);
        }
        Collections.sort(names);
        for (String shortName2 : names) {
            result.put(shortName2, outcomes.get(classNames.get(shortName2)));
        }
        return result;
    }

    private void addMatchLogMessage(StringBuilder message, String source, ConditionEvaluationReport.ConditionAndOutcomes matches) {
        message.append(String.format("%n   %s matched:%n", source));
        Iterator<ConditionEvaluationReport.ConditionAndOutcome> it = matches.iterator();
        while (it.hasNext()) {
            ConditionEvaluationReport.ConditionAndOutcome match = it.next();
            logConditionAndOutcome(message, "      ", match);
        }
    }

    private void addNonMatchLogMessage(StringBuilder message, String source, ConditionEvaluationReport.ConditionAndOutcomes conditionAndOutcomes) {
        message.append(String.format("%n   %s:%n", source));
        List<ConditionEvaluationReport.ConditionAndOutcome> matches = new ArrayList<>();
        List<ConditionEvaluationReport.ConditionAndOutcome> nonMatches = new ArrayList<>();
        Iterator<ConditionEvaluationReport.ConditionAndOutcome> it = conditionAndOutcomes.iterator();
        while (it.hasNext()) {
            ConditionEvaluationReport.ConditionAndOutcome conditionAndOutcome = it.next();
            if (conditionAndOutcome.getOutcome().isMatch()) {
                matches.add(conditionAndOutcome);
            } else {
                nonMatches.add(conditionAndOutcome);
            }
        }
        message.append(String.format("      Did not match:%n", new Object[0]));
        for (ConditionEvaluationReport.ConditionAndOutcome nonMatch : nonMatches) {
            logConditionAndOutcome(message, "         ", nonMatch);
        }
        if (!matches.isEmpty()) {
            message.append(String.format("      Matched:%n", new Object[0]));
            for (ConditionEvaluationReport.ConditionAndOutcome match : matches) {
                logConditionAndOutcome(message, "         ", match);
            }
        }
    }

    private void logConditionAndOutcome(StringBuilder message, String indent, ConditionEvaluationReport.ConditionAndOutcome conditionAndOutcome) {
        message.append(String.format("%s- ", indent));
        String outcomeMessage = conditionAndOutcome.getOutcome().getMessage();
        if (StringUtils.hasLength(outcomeMessage)) {
            message.append(outcomeMessage);
        } else {
            message.append(conditionAndOutcome.getOutcome().isMatch() ? "matched" : "did not match");
        }
        message.append(" (");
        message.append(ClassUtils.getShortName(conditionAndOutcome.getCondition().getClass()));
        message.append(String.format(")%n", new Object[0]));
    }

    public String toString() {
        return this.message.toString();
    }
}
