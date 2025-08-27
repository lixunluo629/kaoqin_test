package com.graphbuilder.math;

import com.graphbuilder.math.func.AbsFunction;
import com.graphbuilder.math.func.AcosFunction;
import com.graphbuilder.math.func.AcoshFunction;
import com.graphbuilder.math.func.AsinFunction;
import com.graphbuilder.math.func.AsinhFunction;
import com.graphbuilder.math.func.AtanFunction;
import com.graphbuilder.math.func.AtanhFunction;
import com.graphbuilder.math.func.AvgFunction;
import com.graphbuilder.math.func.CeilFunction;
import com.graphbuilder.math.func.CombinFunction;
import com.graphbuilder.math.func.CosFunction;
import com.graphbuilder.math.func.CoshFunction;
import com.graphbuilder.math.func.EFunction;
import com.graphbuilder.math.func.ExpFunction;
import com.graphbuilder.math.func.FactFunction;
import com.graphbuilder.math.func.FloorFunction;
import com.graphbuilder.math.func.Function;
import com.graphbuilder.math.func.LgFunction;
import com.graphbuilder.math.func.LnFunction;
import com.graphbuilder.math.func.LogFunction;
import com.graphbuilder.math.func.MaxFunction;
import com.graphbuilder.math.func.MinFunction;
import com.graphbuilder.math.func.ModFunction;
import com.graphbuilder.math.func.PiFunction;
import com.graphbuilder.math.func.PowFunction;
import com.graphbuilder.math.func.RandFunction;
import com.graphbuilder.math.func.RoundFunction;
import com.graphbuilder.math.func.SignFunction;
import com.graphbuilder.math.func.SinFunction;
import com.graphbuilder.math.func.SinhFunction;
import com.graphbuilder.math.func.SqrtFunction;
import com.graphbuilder.math.func.SumFunction;
import com.graphbuilder.math.func.TanFunction;
import com.graphbuilder.math.func.TanhFunction;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.jsonwebtoken.Claims;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/FuncMap.class */
public class FuncMap {
    private String[] name;
    private Function[] func;
    private int numFunc;
    private boolean caseSensitive;

    public FuncMap() {
        this.name = new String[50];
        this.func = new Function[50];
        this.numFunc = 0;
        this.caseSensitive = false;
    }

    public FuncMap(boolean caseSensitive) {
        this.name = new String[50];
        this.func = new Function[50];
        this.numFunc = 0;
        this.caseSensitive = false;
        this.caseSensitive = caseSensitive;
    }

    public void loadDefaultFunctions() {
        setFunction("min", new MinFunction());
        setFunction("max", new MaxFunction());
        setFunction("sum", new SumFunction());
        setFunction("avg", new AvgFunction());
        setFunction("pi", new PiFunction());
        setFunction("e", new EFunction());
        setFunction("rand", new RandFunction());
        setFunction("sin", new SinFunction());
        setFunction("cos", new CosFunction());
        setFunction("tan", new TanFunction());
        setFunction("sqrt", new SqrtFunction());
        setFunction("abs", new AbsFunction());
        setFunction("ceil", new CeilFunction());
        setFunction("floor", new FloorFunction());
        setFunction(Claims.EXPIRATION, new ExpFunction());
        setFunction("lg", new LgFunction());
        setFunction("ln", new LnFunction());
        setFunction("sign", new SignFunction());
        setFunction("round", new RoundFunction());
        setFunction("fact", new FactFunction());
        setFunction("cosh", new CoshFunction());
        setFunction("sinh", new SinhFunction());
        setFunction("tanh", new TanhFunction());
        setFunction("acos", new AcosFunction());
        setFunction("asin", new AsinFunction());
        setFunction("atan", new AtanFunction());
        setFunction("acosh", new AcoshFunction());
        setFunction("asinh", new AsinhFunction());
        setFunction("atanh", new AtanhFunction());
        setFunction("pow", new PowFunction());
        setFunction("mod", new ModFunction());
        setFunction("combin", new CombinFunction());
        setFunction("log", new LogFunction());
    }

    public Function getFunction(String funcName, int numParam) {
        for (int i = 0; i < this.numFunc; i++) {
            if (this.func[i].acceptNumParam(numParam) && ((this.caseSensitive && this.name[i].equals(funcName)) || (!this.caseSensitive && this.name[i].equalsIgnoreCase(funcName)))) {
                return this.func[i];
            }
        }
        throw new RuntimeException("function not found: " + funcName + SymbolConstants.SPACE_SYMBOL + numParam);
    }

    public void setFunction(String funcName, Function f) {
        if (funcName == null) {
            throw new IllegalArgumentException("function name cannot be null");
        }
        if (f == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        for (int i = 0; i < this.numFunc; i++) {
            if ((this.caseSensitive && this.name[i].equals(funcName)) || (!this.caseSensitive && this.name[i].equalsIgnoreCase(funcName))) {
                this.func[i] = f;
                return;
            }
        }
        if (this.numFunc == this.name.length) {
            String[] tmp1 = new String[2 * this.numFunc];
            Function[] tmp2 = new Function[tmp1.length];
            for (int i2 = 0; i2 < this.numFunc; i2++) {
                tmp1[i2] = this.name[i2];
                tmp2[i2] = this.func[i2];
            }
            this.name = tmp1;
            this.func = tmp2;
        }
        this.name[this.numFunc] = funcName;
        this.func[this.numFunc] = f;
        this.numFunc++;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public String[] getFunctionNames() {
        String[] arr = new String[this.numFunc];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = this.name[i];
        }
        return arr;
    }

    public Function[] getFunctions() {
        Function[] arr = new Function[this.numFunc];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = this.func[i];
        }
        return arr;
    }

    public void remove(String funcName) {
        for (int i = 0; i < this.numFunc; i++) {
            if ((this.caseSensitive && this.name[i].equals(funcName)) || (!this.caseSensitive && this.name[i].equalsIgnoreCase(funcName))) {
                for (int j = i + 1; j < this.numFunc; j++) {
                    this.name[j - 1] = this.name[j];
                    this.func[j - 1] = this.func[j];
                }
                this.numFunc--;
                this.name[this.numFunc] = null;
                this.func[this.numFunc] = null;
                return;
            }
        }
    }
}
