package com.itextpdf.layout.tagging;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.TagStructureContext;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.kernel.pdf.tagutils.WaitingTagsManager;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.ILargeElement;
import com.itextpdf.layout.renderer.AreaBreakRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/tagging/LayoutTaggingHelper.class */
public class LayoutTaggingHelper {
    private TagStructureContext context;
    private PdfDocument document;
    private boolean immediateFlush;
    private Map<PdfObject, TaggingDummyElement> existingTagsDummies;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final int RETVAL_NO_PARENT = -1;
    private final int RETVAL_PARENT_AND_KID_FINISHED = -2;
    private Map<TaggingHintKey, List<TaggingHintKey>> kidsHints = new LinkedHashMap();
    private Map<TaggingHintKey, TaggingHintKey> parentHints = new LinkedHashMap();
    private Map<IRenderer, TagTreePointer> autoTaggingPointerSavedPosition = new HashMap();
    private Map<String, List<ITaggingRule>> taggingRules = new HashMap();

    static {
        $assertionsDisabled = !LayoutTaggingHelper.class.desiredAssertionStatus();
    }

    public LayoutTaggingHelper(PdfDocument document, boolean immediateFlush) {
        this.document = document;
        this.context = document.getTagStructureContext();
        this.immediateFlush = immediateFlush;
        registerRules(this.context.getTagStructureTargetVersion());
        this.existingTagsDummies = new LinkedHashMap();
    }

    public static void addTreeHints(LayoutTaggingHelper taggingHelper, IRenderer rootRenderer) {
        List<IRenderer> childRenderers = rootRenderer.getChildRenderers();
        if (childRenderers == null) {
            return;
        }
        taggingHelper.addKidsHint(rootRenderer, childRenderers);
        for (IRenderer childRenderer : childRenderers) {
            addTreeHints(taggingHelper, childRenderer);
        }
    }

    public static TaggingHintKey getHintKey(IPropertyContainer container) {
        return (TaggingHintKey) container.getProperty(109);
    }

    public static TaggingHintKey getOrCreateHintKey(IPropertyContainer container) {
        return getOrCreateHintKey(container, true);
    }

    public void addKidsHint(TagTreePointer parentPointer, Iterable<? extends IPropertyContainer> newKids) {
        PdfDictionary pointerStructElem = this.context.getPointerStructElem(parentPointer).getPdfObject();
        TaggingDummyElement dummy = this.existingTagsDummies.get(pointerStructElem);
        if (dummy == null) {
            dummy = new TaggingDummyElement(parentPointer.getRole());
            this.existingTagsDummies.put(pointerStructElem, dummy);
        }
        this.context.getWaitingTagsManager().assignWaitingState(parentPointer, getOrCreateHintKey(dummy));
        addKidsHint(dummy, newKids);
    }

    public void addKidsHint(IPropertyContainer parent, Iterable<? extends IPropertyContainer> newKids) {
        addKidsHint(parent, newKids, -1);
    }

    public void addKidsHint(IPropertyContainer parent, Iterable<? extends IPropertyContainer> newKids, int insertIndex) {
        if (parent instanceof AreaBreakRenderer) {
            return;
        }
        TaggingHintKey parentKey = getOrCreateHintKey(parent);
        List<TaggingHintKey> newKidsKeys = new ArrayList<>();
        for (IPropertyContainer kid : newKids) {
            if (kid instanceof AreaBreakRenderer) {
                return;
            } else {
                newKidsKeys.add(getOrCreateHintKey(kid));
            }
        }
        addKidsHint(parentKey, newKidsKeys, insertIndex);
    }

    public void addKidsHint(TaggingHintKey parentKey, Collection<TaggingHintKey> newKidsKeys) {
        addKidsHint(parentKey, newKidsKeys, -1);
    }

    public void addKidsHint(TaggingHintKey parentKey, Collection<TaggingHintKey> newKidsKeys, int insertIndex) {
        addKidsHint(parentKey, newKidsKeys, insertIndex, false);
    }

    public void setRoleHint(IPropertyContainer hintOwner, String role) {
        getOrCreateHintKey(hintOwner).setOverriddenRole(role);
    }

    public boolean isArtifact(IPropertyContainer hintOwner) {
        TaggingHintKey key = getHintKey(hintOwner);
        if (key != null) {
            return key.isArtifact();
        }
        IAccessibleElement aElem = null;
        if ((hintOwner instanceof IRenderer) && (((IRenderer) hintOwner).getModelElement() instanceof IAccessibleElement)) {
            aElem = (IAccessibleElement) ((IRenderer) hintOwner).getModelElement();
        } else if (hintOwner instanceof IAccessibleElement) {
            aElem = (IAccessibleElement) hintOwner;
        }
        if (aElem != null) {
            return StandardRoles.ARTIFACT.equals(aElem.getAccessibilityProperties().getRole());
        }
        return false;
    }

    public void markArtifactHint(IPropertyContainer hintOwner) {
        TaggingHintKey hintKey = getOrCreateHintKey(hintOwner);
        markArtifactHint(hintKey);
    }

    public void markArtifactHint(TaggingHintKey hintKey) {
        hintKey.setArtifact();
        hintKey.setFinished();
        TagTreePointer existingArtifactTag = new TagTreePointer(this.document);
        if (this.context.getWaitingTagsManager().tryMovePointerToWaitingTag(existingArtifactTag, hintKey)) {
            Logger logger = LoggerFactory.getLogger((Class<?>) LayoutTaggingHelper.class);
            logger.error(LogMessageConstant.ALREADY_TAGGED_HINT_MARKED_ARTIFACT);
            this.context.getWaitingTagsManager().removeWaitingState(hintKey);
            if (this.immediateFlush) {
                existingArtifactTag.flushParentsIfAllKidsFlushed();
            }
        }
        List<TaggingHintKey> kidsHint = getKidsHint(hintKey);
        for (TaggingHintKey kidKey : kidsHint) {
            markArtifactHint(kidKey);
        }
        removeParentHint(hintKey);
    }

    public TagTreePointer useAutoTaggingPointerAndRememberItsPosition(IRenderer renderer) {
        TagTreePointer autoTaggingPointer = this.context.getAutoTaggingPointer();
        TagTreePointer position = new TagTreePointer(autoTaggingPointer);
        this.autoTaggingPointerSavedPosition.put(renderer, position);
        return autoTaggingPointer;
    }

    public void restoreAutoTaggingPointerPosition(IRenderer renderer) {
        TagTreePointer autoTaggingPointer = this.context.getAutoTaggingPointer();
        TagTreePointer position = this.autoTaggingPointerSavedPosition.remove(renderer);
        if (position != null) {
            autoTaggingPointer.moveToPointer(position);
        }
    }

    public List<TaggingHintKey> getKidsHint(TaggingHintKey parent) {
        List<TaggingHintKey> kidsHint = this.kidsHints.get(parent);
        if (kidsHint == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(kidsHint);
    }

    public List<TaggingHintKey> getAccessibleKidsHint(TaggingHintKey parent) {
        List<TaggingHintKey> kidsHint = this.kidsHints.get(parent);
        if (kidsHint == null) {
            return Collections.emptyList();
        }
        List<TaggingHintKey> accessibleKids = new ArrayList<>();
        for (TaggingHintKey kid : kidsHint) {
            if (isNonAccessibleHint(kid)) {
                accessibleKids.addAll(getAccessibleKidsHint(kid));
            } else {
                accessibleKids.add(kid);
            }
        }
        return accessibleKids;
    }

    public TaggingHintKey getParentHint(IPropertyContainer hintOwner) {
        TaggingHintKey hintKey = getHintKey(hintOwner);
        if (hintKey == null) {
            return null;
        }
        return getParentHint(hintKey);
    }

    public TaggingHintKey getParentHint(TaggingHintKey hintKey) {
        return this.parentHints.get(hintKey);
    }

    public TaggingHintKey getAccessibleParentHint(TaggingHintKey hintKey) {
        do {
            hintKey = getParentHint(hintKey);
            if (hintKey == null) {
                break;
            }
        } while (isNonAccessibleHint(hintKey));
        return hintKey;
    }

    public void releaseFinishedHints() {
        Set<TaggingHintKey> allHints = new HashSet<>();
        for (Map.Entry<TaggingHintKey, TaggingHintKey> entry : this.parentHints.entrySet()) {
            allHints.add(entry.getKey());
            allHints.add(entry.getValue());
        }
        for (TaggingHintKey hint : allHints) {
            if (hint.isFinished() && !isNonAccessibleHint(hint) && !(hint.getAccessibleElement() instanceof TaggingDummyElement)) {
                finishDummyKids(getKidsHint(hint));
            }
        }
        for (TaggingHintKey hint2 : allHints) {
            if (hint2.isFinished()) {
                releaseHint(hint2, true);
            }
        }
    }

    public void releaseAllHints() {
        for (TaggingDummyElement dummy : this.existingTagsDummies.values()) {
            finishTaggingHint(dummy);
            finishDummyKids(getKidsHint(getHintKey(dummy)));
        }
        this.existingTagsDummies.clear();
        releaseFinishedHints();
        Set<TaggingHintKey> hangingHints = new HashSet<>();
        for (Map.Entry<TaggingHintKey, TaggingHintKey> entry : this.parentHints.entrySet()) {
            hangingHints.add(entry.getKey());
            hangingHints.add(entry.getValue());
        }
        for (TaggingHintKey hint : hangingHints) {
            releaseHint(hint, false);
        }
        if (!$assertionsDisabled && !this.parentHints.isEmpty()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !this.kidsHints.isEmpty()) {
            throw new AssertionError();
        }
    }

    public boolean createTag(IRenderer renderer, TagTreePointer tagPointer) {
        TaggingHintKey hintKey = getHintKey(renderer);
        boolean noHint = hintKey == null;
        if (noHint) {
            hintKey = getOrCreateHintKey(renderer, false);
        }
        boolean created = createTag(hintKey, tagPointer);
        if (noHint) {
            hintKey.setFinished();
            this.context.getWaitingTagsManager().removeWaitingState(hintKey);
        }
        return created;
    }

    public boolean createTag(TaggingHintKey hintKey, TagTreePointer tagPointer) {
        if (hintKey.isArtifact()) {
            return false;
        }
        boolean created = createSingleTag(hintKey, tagPointer);
        if (created) {
            List<TaggingHintKey> kidsHint = getAccessibleKidsHint(hintKey);
            for (TaggingHintKey hint : kidsHint) {
                if (hint.getAccessibleElement() instanceof TaggingDummyElement) {
                    createTag(hint, new TagTreePointer(this.document));
                }
            }
        }
        return created;
    }

    public void finishTaggingHint(IPropertyContainer hintOwner) {
        TaggingHintKey rendererKey = getHintKey(hintOwner);
        if (rendererKey == null || rendererKey.isFinished()) {
            return;
        }
        if (rendererKey.isElementBasedFinishingOnly() && !(hintOwner instanceof IElement)) {
            return;
        }
        if (!isNonAccessibleHint(rendererKey)) {
            IAccessibleElement modelElement = rendererKey.getAccessibleElement();
            String role = modelElement.getAccessibilityProperties().getRole();
            if (rendererKey.getOverriddenRole() != null) {
                role = rendererKey.getOverriddenRole();
            }
            List<ITaggingRule> rules = this.taggingRules.get(role);
            boolean ruleResult = true;
            if (rules != null) {
                for (ITaggingRule rule : rules) {
                    ruleResult = ruleResult && rule.onTagFinish(this, rendererKey);
                }
            }
            if (!ruleResult) {
                return;
            }
        }
        rendererKey.setFinished();
    }

    public int replaceKidHint(TaggingHintKey kidHintKey, Collection<TaggingHintKey> newKidsHintKeys) {
        TaggingHintKey parentKey = getParentHint(kidHintKey);
        if (parentKey == null) {
            return -1;
        }
        if (kidHintKey.isFinished()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) LayoutTaggingHelper.class);
            logger.error(LogMessageConstant.CANNOT_REPLACE_FINISHED_HINT);
            return -1;
        }
        int kidIndex = removeParentHint(kidHintKey);
        List<TaggingHintKey> kidsToBeAdded = new ArrayList<>();
        for (TaggingHintKey newKidKey : newKidsHintKeys) {
            int i = removeParentHint(newKidKey);
            if (i == -2 || (i == -1 && newKidKey.isFinished())) {
                Logger logger2 = LoggerFactory.getLogger((Class<?>) LayoutTaggingHelper.class);
                logger2.error(LogMessageConstant.CANNOT_MOVE_FINISHED_HINT);
            } else {
                kidsToBeAdded.add(newKidKey);
            }
        }
        addKidsHint(parentKey, kidsToBeAdded, kidIndex, true);
        return kidIndex;
    }

    public int moveKidHint(TaggingHintKey hintKeyOfKidToMove, TaggingHintKey newParent) {
        return moveKidHint(hintKeyOfKidToMove, newParent, -1);
    }

    public int moveKidHint(TaggingHintKey hintKeyOfKidToMove, TaggingHintKey newParent, int insertIndex) {
        if (newParent.isFinished()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) LayoutTaggingHelper.class);
            logger.error(LogMessageConstant.CANNOT_MOVE_HINT_TO_FINISHED_PARENT);
            return -1;
        }
        int removeRes = removeParentHint(hintKeyOfKidToMove);
        if (removeRes == -2 || (removeRes == -1 && hintKeyOfKidToMove.isFinished())) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) LayoutTaggingHelper.class);
            logger2.error(LogMessageConstant.CANNOT_MOVE_FINISHED_HINT);
            return -1;
        }
        addKidsHint(newParent, Collections.singletonList(hintKeyOfKidToMove), insertIndex, true);
        return removeRes;
    }

    public PdfDocument getPdfDocument() {
        return this.document;
    }

    private static TaggingHintKey getOrCreateHintKey(IPropertyContainer hintOwner, boolean setProperty) {
        TaggingHintKey hintKey = (TaggingHintKey) hintOwner.getProperty(109);
        if (hintKey == null) {
            IAccessibleElement elem = null;
            if (hintOwner instanceof IAccessibleElement) {
                elem = (IAccessibleElement) hintOwner;
            } else if ((hintOwner instanceof IRenderer) && (((IRenderer) hintOwner).getModelElement() instanceof IAccessibleElement)) {
                elem = (IAccessibleElement) ((IRenderer) hintOwner).getModelElement();
            }
            hintKey = new TaggingHintKey(elem, hintOwner instanceof IElement);
            if (elem != null && StandardRoles.ARTIFACT.equals(elem.getAccessibilityProperties().getRole())) {
                hintKey.setArtifact();
                hintKey.setFinished();
            }
            if (setProperty) {
                if ((elem instanceof ILargeElement) && !((ILargeElement) elem).isComplete()) {
                    ((ILargeElement) elem).setProperty(109, hintKey);
                } else {
                    hintOwner.setProperty(109, hintKey);
                }
            }
        }
        return hintKey;
    }

    private void addKidsHint(TaggingHintKey parentKey, Collection<TaggingHintKey> newKidsKeys, int insertIndex, boolean skipFinishedChecks) {
        if (newKidsKeys.isEmpty()) {
            return;
        }
        if (parentKey.isArtifact()) {
            for (TaggingHintKey kid : newKidsKeys) {
                markArtifactHint(kid);
            }
            return;
        }
        if (!skipFinishedChecks && parentKey.isFinished()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) LayoutTaggingHelper.class);
            logger.error(LogMessageConstant.CANNOT_ADD_HINTS_TO_FINISHED_PARENT);
            return;
        }
        List<TaggingHintKey> kidsHint = this.kidsHints.get(parentKey);
        if (kidsHint == null) {
            kidsHint = new ArrayList();
        }
        TaggingHintKey parentTagHint = isNonAccessibleHint(parentKey) ? getAccessibleParentHint(parentKey) : parentKey;
        boolean parentTagAlreadyCreated = parentTagHint != null && isTagAlreadyExistsForHint(parentTagHint);
        for (TaggingHintKey kidKey : newKidsKeys) {
            if (!kidKey.isArtifact()) {
                TaggingHintKey prevParent = getParentHint(kidKey);
                if (prevParent == null) {
                    if (!skipFinishedChecks && kidKey.isFinished()) {
                        Logger logger2 = LoggerFactory.getLogger((Class<?>) LayoutTaggingHelper.class);
                        logger2.error(LogMessageConstant.CANNOT_ADD_FINISHED_HINT_AS_A_NEW_KID_HINT);
                    } else {
                        if (insertIndex > -1) {
                            int i = insertIndex;
                            insertIndex++;
                            kidsHint.add(i, kidKey);
                        } else {
                            kidsHint.add(kidKey);
                        }
                        this.parentHints.put(kidKey, parentKey);
                        if (parentTagAlreadyCreated) {
                            if (kidKey.getAccessibleElement() instanceof TaggingDummyElement) {
                                createTag(kidKey, new TagTreePointer(this.document));
                            }
                            if (isNonAccessibleHint(kidKey)) {
                                for (TaggingHintKey nestedKid : getAccessibleKidsHint(kidKey)) {
                                    if (nestedKid.getAccessibleElement() instanceof TaggingDummyElement) {
                                        createTag(nestedKid, new TagTreePointer(this.document));
                                    }
                                    moveKidTagIfCreated(parentTagHint, nestedKid);
                                }
                            } else {
                                moveKidTagIfCreated(parentTagHint, kidKey);
                            }
                        }
                    }
                }
            }
        }
        if (!kidsHint.isEmpty()) {
            this.kidsHints.put(parentKey, kidsHint);
        }
    }

    private boolean createSingleTag(TaggingHintKey hintKey, TagTreePointer tagPointer) {
        if (hintKey.isFinished()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) LayoutTaggingHelper.class);
            logger.error(LogMessageConstant.ATTEMPT_TO_CREATE_A_TAG_FOR_FINISHED_HINT);
            return false;
        }
        if (isNonAccessibleHint(hintKey)) {
            TaggingHintKey parentTagHint = getAccessibleParentHint(hintKey);
            this.context.getWaitingTagsManager().tryMovePointerToWaitingTag(tagPointer, parentTagHint);
            return false;
        }
        WaitingTagsManager waitingTagsManager = this.context.getWaitingTagsManager();
        if (!waitingTagsManager.tryMovePointerToWaitingTag(tagPointer, hintKey)) {
            IAccessibleElement modelElement = hintKey.getAccessibleElement();
            TaggingHintKey parentHint = getAccessibleParentHint(hintKey);
            int ind = -1;
            if (parentHint != null && waitingTagsManager.tryMovePointerToWaitingTag(tagPointer, parentHint)) {
                List<TaggingHintKey> siblingsHint = getAccessibleKidsHint(parentHint);
                int i = siblingsHint.indexOf(hintKey);
                ind = getNearestNextSiblingTagIndex(waitingTagsManager, tagPointer, siblingsHint, i);
            }
            tagPointer.addTag(ind, modelElement.getAccessibilityProperties());
            if (hintKey.getOverriddenRole() != null) {
                tagPointer.setRole(hintKey.getOverriddenRole());
            }
            waitingTagsManager.assignWaitingState(tagPointer, hintKey);
            List<TaggingHintKey> kidsHint = getAccessibleKidsHint(hintKey);
            for (TaggingHintKey kidKey : kidsHint) {
                moveKidTagIfCreated(hintKey, kidKey);
            }
            return true;
        }
        return false;
    }

    private int removeParentHint(TaggingHintKey hintKey) {
        TaggingHintKey parentHint = this.parentHints.get(hintKey);
        if (parentHint == null) {
            return -1;
        }
        TaggingHintKey accessibleParentHint = getAccessibleParentHint(hintKey);
        if (hintKey.isFinished() && parentHint.isFinished() && (accessibleParentHint == null || accessibleParentHint.isFinished())) {
            return -2;
        }
        return removeParentHint(hintKey, parentHint);
    }

    private int removeParentHint(TaggingHintKey hintKey, TaggingHintKey parentHint) {
        this.parentHints.remove(hintKey);
        List<TaggingHintKey> kidsHint = this.kidsHints.get(parentHint);
        int size = kidsHint.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            if (kidsHint.get(i) != hintKey) {
                i++;
            } else {
                kidsHint.remove(i);
                break;
            }
        }
        if (!$assertionsDisabled && i >= size) {
            throw new AssertionError();
        }
        if (kidsHint.isEmpty()) {
            this.kidsHints.remove(parentHint);
        }
        return i;
    }

    private void finishDummyKids(List<TaggingHintKey> taggingHintKeys) {
        for (TaggingHintKey hintKey : taggingHintKeys) {
            boolean isDummy = hintKey.getAccessibleElement() instanceof TaggingDummyElement;
            if (isDummy) {
                finishTaggingHint((IPropertyContainer) hintKey.getAccessibleElement());
            }
            if (isNonAccessibleHint(hintKey) || isDummy) {
                finishDummyKids(getKidsHint(hintKey));
            }
        }
    }

    private void moveKidTagIfCreated(TaggingHintKey parentKey, TaggingHintKey kidKey) {
        TagTreePointer kidPointer = new TagTreePointer(this.document);
        WaitingTagsManager waitingTagsManager = this.context.getWaitingTagsManager();
        if (!waitingTagsManager.tryMovePointerToWaitingTag(kidPointer, kidKey)) {
            return;
        }
        TagTreePointer parentPointer = new TagTreePointer(this.document);
        if (!waitingTagsManager.tryMovePointerToWaitingTag(parentPointer, parentKey)) {
            return;
        }
        int kidIndInParentKidsHint = getAccessibleKidsHint(parentKey).indexOf(kidKey);
        int ind = getNearestNextSiblingTagIndex(waitingTagsManager, parentPointer, getAccessibleKidsHint(parentKey), kidIndInParentKidsHint);
        parentPointer.setNextNewKidIndex(ind);
        kidPointer.relocate(parentPointer);
    }

    private int getNearestNextSiblingTagIndex(WaitingTagsManager waitingTagsManager, TagTreePointer parentPointer, List<TaggingHintKey> siblingsHint, int start) {
        int ind = -1;
        TagTreePointer nextSiblingPointer = new TagTreePointer(this.document);
        while (true) {
            start++;
            if (start < siblingsHint.size()) {
                if (waitingTagsManager.tryMovePointerToWaitingTag(nextSiblingPointer, siblingsHint.get(start)) && parentPointer.isPointingToSameTag(new TagTreePointer(nextSiblingPointer).moveToParent())) {
                    ind = nextSiblingPointer.getIndexInParentKidsList();
                    break;
                }
            } else {
                break;
            }
        }
        return ind;
    }

    private static boolean isNonAccessibleHint(TaggingHintKey hintKey) {
        return hintKey.getAccessibleElement() == null || hintKey.getAccessibleElement().getAccessibilityProperties().getRole() == null;
    }

    private boolean isTagAlreadyExistsForHint(TaggingHintKey tagHint) {
        return this.context.getWaitingTagsManager().isObjectAssociatedWithWaitingTag(tagHint);
    }

    private void releaseHint(TaggingHintKey hint, boolean checkContextIsFinished) {
        TaggingHintKey parentHint = this.parentHints.get(hint);
        List<TaggingHintKey> kidsHint = this.kidsHints.get(hint);
        if (checkContextIsFinished && parentHint != null && isSomeParentNotFinished(parentHint)) {
            return;
        }
        if (checkContextIsFinished && kidsHint != null && isSomeKidNotFinished(hint)) {
            return;
        }
        if (parentHint != null) {
            removeParentHint(hint, parentHint);
        }
        if (kidsHint != null) {
            for (TaggingHintKey kidHint : kidsHint) {
                this.parentHints.remove(kidHint);
            }
            this.kidsHints.remove(hint);
        }
        TagTreePointer tagPointer = new TagTreePointer(this.document);
        if (this.context.getWaitingTagsManager().tryMovePointerToWaitingTag(tagPointer, hint)) {
            this.context.getWaitingTagsManager().removeWaitingState(hint);
            if (this.immediateFlush) {
                tagPointer.flushParentsIfAllKidsFlushed();
                return;
            }
            return;
        }
        this.context.getWaitingTagsManager().removeWaitingState(hint);
    }

    private boolean isSomeParentNotFinished(TaggingHintKey parentHint) {
        TaggingHintKey parentHint2 = parentHint;
        while (true) {
            TaggingHintKey hintKey = parentHint2;
            if (hintKey == null) {
                return false;
            }
            if (!hintKey.isFinished()) {
                return true;
            }
            if (!isNonAccessibleHint(hintKey)) {
                return false;
            }
            parentHint2 = getParentHint(hintKey);
        }
    }

    private boolean isSomeKidNotFinished(TaggingHintKey hint) {
        for (TaggingHintKey kidHint : getKidsHint(hint)) {
            if (!kidHint.isFinished()) {
                return true;
            }
            if (isNonAccessibleHint(kidHint) && isSomeKidNotFinished(kidHint)) {
                return true;
            }
        }
        return false;
    }

    private void registerRules(PdfVersion pdfVersion) {
        ITaggingRule tableRule = new TableTaggingRule();
        registerSingleRule(StandardRoles.TABLE, tableRule);
        registerSingleRule(StandardRoles.TFOOT, tableRule);
        registerSingleRule(StandardRoles.THEAD, tableRule);
        if (pdfVersion.compareTo(PdfVersion.PDF_1_5) < 0) {
            TableTaggingPriorToOneFiveVersionRule priorToOneFiveRule = new TableTaggingPriorToOneFiveVersionRule();
            registerSingleRule(StandardRoles.TABLE, priorToOneFiveRule);
            registerSingleRule(StandardRoles.THEAD, priorToOneFiveRule);
            registerSingleRule(StandardRoles.TFOOT, priorToOneFiveRule);
        }
    }

    private void registerSingleRule(String role, ITaggingRule rule) {
        List<ITaggingRule> rules = this.taggingRules.get(role);
        if (rules == null) {
            rules = new ArrayList();
            this.taggingRules.put(role, rules);
        }
        rules.add(rule);
    }
}
