package com.itextpdf.kernel.pdf.action;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.filespec.PdfStringFS;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitRemoteGoToDestination;
import com.itextpdf.kernel.pdf.navigation.PdfStringDestination;
import com.itextpdf.kernel.pdf.navigation.PdfStructureDestination;
import java.util.List;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/action/PdfAction.class */
public class PdfAction extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -3945353673249710860L;
    public static final int SUBMIT_EXCLUDE = 1;
    public static final int SUBMIT_INCLUDE_NO_VALUE_FIELDS = 2;
    public static final int SUBMIT_HTML_FORMAT = 4;
    public static final int SUBMIT_HTML_GET = 8;
    public static final int SUBMIT_COORDINATES = 16;
    public static final int SUBMIT_XFDF = 32;
    public static final int SUBMIT_INCLUDE_APPEND_SAVES = 64;
    public static final int SUBMIT_INCLUDE_ANNOTATIONS = 128;
    public static final int SUBMIT_PDF = 256;
    public static final int SUBMIT_CANONICAL_FORMAT = 512;
    public static final int SUBMIT_EXCL_NON_USER_ANNOTS = 1024;
    public static final int SUBMIT_EXCL_F_KEY = 2048;
    public static final int SUBMIT_EMBED_FORM = 8196;
    public static final int RESET_EXCLUDE = 1;

    public PdfAction() {
        this(new PdfDictionary());
        put(PdfName.Type, PdfName.Action);
    }

    public PdfAction(PdfDictionary pdfObject) {
        super(pdfObject);
        markObjectAsIndirect(getPdfObject());
    }

    public static PdfAction createGoTo(PdfDestination destination) {
        validateNotRemoteDestination(destination);
        return new PdfAction().put(PdfName.S, PdfName.GoTo).put(PdfName.D, destination.getPdfObject());
    }

    public static PdfAction createGoTo(String destination) {
        return createGoTo(new PdfStringDestination(destination));
    }

    public static PdfAction createGoToR(PdfFileSpec fileSpec, PdfDestination destination, boolean newWindow) {
        return createGoToR(fileSpec, destination).put(PdfName.NewWindow, PdfBoolean.valueOf(newWindow));
    }

    public static PdfAction createGoToR(PdfFileSpec fileSpec, PdfDestination destination) {
        validateRemoteDestination(destination);
        return new PdfAction().put(PdfName.S, PdfName.GoToR).put(PdfName.F, fileSpec.getPdfObject()).put(PdfName.D, destination.getPdfObject());
    }

    public static PdfAction createGoToR(String filename, int pageNum) {
        return createGoToR(filename, pageNum, false);
    }

    public static PdfAction createGoToR(String filename, int pageNum, boolean newWindow) {
        return createGoToR(new PdfStringFS(filename), PdfExplicitRemoteGoToDestination.createFitH(pageNum, 10000.0f), newWindow);
    }

    public static PdfAction createGoToR(String filename, String destination, boolean newWindow) {
        return createGoToR(new PdfStringFS(filename), new PdfStringDestination(destination), newWindow);
    }

    public static PdfAction createGoToR(String filename, String destination) {
        return createGoToR(filename, destination, false);
    }

    public static PdfAction createGoToE(PdfDestination destination, boolean newWindow, PdfTarget targetDictionary) {
        return createGoToE(null, destination, newWindow, targetDictionary);
    }

    public static PdfAction createGoToE(PdfFileSpec fileSpec, PdfDestination destination, boolean newWindow, PdfTarget targetDictionary) {
        PdfAction action = new PdfAction().put(PdfName.S, PdfName.GoToE).put(PdfName.NewWindow, PdfBoolean.valueOf(newWindow));
        if (fileSpec != null) {
            action.put(PdfName.F, fileSpec.getPdfObject());
        }
        if (destination != null) {
            validateRemoteDestination(destination);
            action.put(PdfName.D, destination.getPdfObject());
        } else {
            LoggerFactory.getLogger((Class<?>) PdfAction.class).warn(LogMessageConstant.EMBEDDED_GO_TO_DESTINATION_NOT_SPECIFIED);
        }
        if (targetDictionary != null) {
            action.put(PdfName.T, targetDictionary.getPdfObject());
        }
        return action;
    }

    public static PdfAction createLaunch(PdfFileSpec fileSpec, boolean newWindow) {
        return createLaunch(fileSpec).put(PdfName.NewWindow, new PdfBoolean(newWindow));
    }

    public static PdfAction createLaunch(PdfFileSpec fileSpec) {
        PdfAction action = new PdfAction().put(PdfName.S, PdfName.Launch);
        if (fileSpec != null) {
            action.put(PdfName.F, fileSpec.getPdfObject());
        }
        return action;
    }

    public static PdfAction createThread(PdfFileSpec fileSpec, PdfObject destinationThread, PdfObject bead) {
        PdfAction action = new PdfAction().put(PdfName.S, PdfName.Launch).put(PdfName.D, destinationThread).put(PdfName.B, bead);
        if (fileSpec != null) {
            action.put(PdfName.F, fileSpec.getPdfObject());
        }
        return action;
    }

    public static PdfAction createThread(PdfFileSpec fileSpec) {
        return createThread(fileSpec, null, null);
    }

    public static PdfAction createURI(String uri) {
        return createURI(uri, false);
    }

    public static PdfAction createURI(String uri, boolean isMap) {
        return new PdfAction().put(PdfName.S, PdfName.URI).put(PdfName.URI, new PdfString(uri)).put(PdfName.IsMap, PdfBoolean.valueOf(isMap));
    }

    public static PdfAction createSound(PdfStream sound) {
        return new PdfAction().put(PdfName.S, PdfName.Sound).put(PdfName.Sound, sound);
    }

    public static PdfAction createSound(PdfStream sound, float volume, boolean synchronous, boolean repeat, boolean mix) {
        if (volume < -1.0f || volume > 1.0f) {
            throw new IllegalArgumentException("volume");
        }
        return new PdfAction().put(PdfName.S, PdfName.Sound).put(PdfName.Sound, sound).put(PdfName.Volume, new PdfNumber(volume)).put(PdfName.Synchronous, PdfBoolean.valueOf(synchronous)).put(PdfName.Repeat, PdfBoolean.valueOf(repeat)).put(PdfName.Mix, PdfBoolean.valueOf(mix));
    }

    public static PdfAction createMovie(PdfAnnotation annotation, String title, PdfName operation) {
        PdfAction action = new PdfAction().put(PdfName.S, PdfName.Movie).put(PdfName.T, new PdfString(title)).put(PdfName.Operation, operation);
        if (annotation != null) {
            action.put(PdfName.Annotation, annotation.getPdfObject());
        }
        return action;
    }

    public static PdfAction createHide(PdfAnnotation annotation, boolean hidden) {
        return new PdfAction().put(PdfName.S, PdfName.Hide).put(PdfName.T, annotation.getPdfObject()).put(PdfName.H, PdfBoolean.valueOf(hidden));
    }

    public static PdfAction createHide(PdfAnnotation[] annotations, boolean hidden) {
        return new PdfAction().put(PdfName.S, PdfName.Hide).put(PdfName.T, getPdfArrayFromAnnotationsList(annotations)).put(PdfName.H, PdfBoolean.valueOf(hidden));
    }

    public static PdfAction createHide(String text, boolean hidden) {
        return new PdfAction().put(PdfName.S, PdfName.Hide).put(PdfName.T, new PdfString(text)).put(PdfName.H, PdfBoolean.valueOf(hidden));
    }

    public static PdfAction createHide(String[] text, boolean hidden) {
        return new PdfAction().put(PdfName.S, PdfName.Hide).put(PdfName.T, getArrayFromStringList(text)).put(PdfName.H, PdfBoolean.valueOf(hidden));
    }

    public static PdfAction createNamed(PdfName namedAction) {
        return new PdfAction().put(PdfName.S, PdfName.Named).put(PdfName.N, namedAction);
    }

    public static PdfAction createSetOcgState(List<PdfActionOcgState> states) {
        return createSetOcgState(states, false);
    }

    public static PdfAction createSetOcgState(List<PdfActionOcgState> states, boolean preserveRb) {
        PdfArray stateArr = new PdfArray();
        for (PdfActionOcgState state : states) {
            stateArr.addAll(state.getObjectList());
        }
        return new PdfAction().put(PdfName.S, PdfName.SetOCGState).put(PdfName.State, stateArr).put(PdfName.PreserveRB, PdfBoolean.valueOf(preserveRb));
    }

    public static PdfAction createRendition(String file, PdfFileSpec fileSpec, String mimeType, PdfAnnotation screenAnnotation) {
        return new PdfAction().put(PdfName.S, PdfName.Rendition).put(PdfName.OP, new PdfNumber(0)).put(PdfName.AN, screenAnnotation.getPdfObject()).put(PdfName.R, new PdfRendition(file, fileSpec, mimeType).getPdfObject());
    }

    public static PdfAction createJavaScript(String javaScript) {
        return new PdfAction().put(PdfName.S, PdfName.JavaScript).put(PdfName.JS, new PdfString(javaScript));
    }

    public static PdfAction createSubmitForm(String file, Object[] names, int flags) {
        PdfAction action = new PdfAction();
        action.put(PdfName.S, PdfName.SubmitForm);
        PdfDictionary urlFileSpec = new PdfDictionary();
        urlFileSpec.put(PdfName.F, new PdfString(file));
        urlFileSpec.put(PdfName.FS, PdfName.URL);
        action.put(PdfName.F, urlFileSpec);
        if (names != null) {
            action.put(PdfName.Fields, buildArray(names));
        }
        action.put(PdfName.Flags, new PdfNumber(flags));
        return action;
    }

    public static PdfAction createResetForm(Object[] names, int flags) {
        PdfAction action = new PdfAction();
        action.put(PdfName.S, PdfName.ResetForm);
        if (names != null) {
            action.put(PdfName.Fields, buildArray(names));
        }
        action.put(PdfName.Flags, new PdfNumber(flags));
        return action;
    }

    public static void setAdditionalAction(PdfObjectWrapper<PdfDictionary> wrapper, PdfName key, PdfAction action) {
        PdfDictionary dic;
        PdfObject obj = ((PdfDictionary) wrapper.getPdfObject()).get(PdfName.AA);
        boolean aaExists = obj != null && obj.isDictionary();
        if (aaExists) {
            dic = (PdfDictionary) obj;
        } else {
            dic = new PdfDictionary();
        }
        dic.put(key, action.getPdfObject());
        dic.setModified();
        ((PdfDictionary) wrapper.getPdfObject()).put(PdfName.AA, dic);
        if (!aaExists || !dic.isIndirect()) {
            ((PdfDictionary) wrapper.getPdfObject()).setModified();
        }
    }

    public void next(PdfAction nextAction) {
        PdfObject currentNextAction = ((PdfDictionary) getPdfObject()).get(PdfName.Next);
        if (currentNextAction == null) {
            put(PdfName.Next, nextAction.getPdfObject());
        } else {
            if (currentNextAction.isDictionary()) {
                PdfArray array = new PdfArray(currentNextAction);
                array.add(nextAction.getPdfObject());
                put(PdfName.Next, array);
                return;
            }
            ((PdfArray) currentNextAction).add(nextAction.getPdfObject());
        }
    }

    public PdfAction put(PdfName key, PdfObject value) {
        getPdfObject().put(key, value);
        setModified();
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        super.flush();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    private static PdfArray getPdfArrayFromAnnotationsList(PdfAnnotation[] wrappers) {
        PdfArray arr = new PdfArray();
        for (PdfAnnotation wrapper : wrappers) {
            arr.add(wrapper.getPdfObject());
        }
        return arr;
    }

    private static PdfArray getArrayFromStringList(String[] strings) {
        PdfArray arr = new PdfArray();
        for (String string : strings) {
            arr.add(new PdfString(string));
        }
        return arr;
    }

    private static PdfArray buildArray(Object[] names) {
        PdfArray array = new PdfArray();
        for (Object obj : names) {
            if (obj instanceof String) {
                array.add(new PdfString((String) obj));
            } else if (obj instanceof PdfAnnotation) {
                array.add(((PdfAnnotation) obj).getPdfObject());
            } else {
                throw new PdfException("The array must contain string or PDFAnnotation");
            }
        }
        return array;
    }

    private static void validateRemoteDestination(PdfDestination destination) {
        if (destination instanceof PdfExplicitDestination) {
            if (((PdfArray) destination.getPdfObject()).get(0).isDictionary()) {
                throw new IllegalArgumentException("Explicit destinations shall specify page number in remote go-to actions instead of page dictionary");
            }
            return;
        }
        if (destination instanceof PdfStructureDestination) {
            PdfObject firstObj = ((PdfArray) destination.getPdfObject()).get(0);
            if (firstObj.isDictionary()) {
                PdfDictionary structElemObj = (PdfDictionary) firstObj;
                PdfString id = structElemObj.getAsString(PdfName.ID);
                if (id == null) {
                    throw new IllegalArgumentException("Structure destinations shall specify structure element ID in remote go-to actions. Structure element that has no ID is specified instead");
                }
                LoggerFactory.getLogger((Class<?>) PdfAction.class).warn(LogMessageConstant.STRUCTURE_ELEMENT_REPLACED_BY_ITS_ID_IN_STRUCTURE_DESTINATION);
                ((PdfArray) destination.getPdfObject()).set(0, id);
                destination.getPdfObject().setModified();
            }
        }
    }

    public static void validateNotRemoteDestination(PdfDestination destination) {
        if (destination instanceof PdfExplicitRemoteGoToDestination) {
            LoggerFactory.getLogger((Class<?>) PdfAction.class).warn(LogMessageConstant.INVALID_DESTINATION_TYPE);
        } else if (destination instanceof PdfExplicitDestination) {
            PdfObject firstObj = ((PdfArray) destination.getPdfObject()).get(0);
            if (firstObj.isNumber()) {
                LoggerFactory.getLogger((Class<?>) PdfAction.class).warn(LogMessageConstant.INVALID_DESTINATION_TYPE);
            }
        }
    }
}
