package com.fasterxml.classmate.members;

import com.fasterxml.classmate.Annotations;
import com.fasterxml.classmate.ResolvedType;
import java.lang.reflect.Constructor;

/* loaded from: classmate-1.3.4.jar:com/fasterxml/classmate/members/ResolvedConstructor.class */
public final class ResolvedConstructor extends ResolvedParameterizedMember<Constructor<?>> {
    public ResolvedConstructor(ResolvedType context, Annotations ann, Constructor<?> constructor, ResolvedType[] argumentTypes) {
        super(context, ann, constructor, null, argumentTypes);
    }
}
