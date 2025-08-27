package com.graphbuilder.math;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/VarMap.class */
public class VarMap {
    private boolean caseSensitive;
    private String[] name;
    private double[] value;
    private int numVars;

    public VarMap() {
        this(true);
    }

    public VarMap(boolean caseSensitive) {
        this.caseSensitive = true;
        this.name = new String[2];
        this.value = new double[2];
        this.numVars = 0;
        this.caseSensitive = caseSensitive;
    }

    public double getValue(String varName) {
        for (int i = 0; i < this.numVars; i++) {
            if ((this.caseSensitive && this.name[i].equals(varName)) || (!this.caseSensitive && this.name[i].equalsIgnoreCase(varName))) {
                return this.value[i];
            }
        }
        throw new RuntimeException("variable value has not been set: " + varName);
    }

    public void setValue(String varName, double val) {
        if (varName == null) {
            throw new IllegalArgumentException("varName cannot be null");
        }
        for (int i = 0; i < this.numVars; i++) {
            if ((this.caseSensitive && this.name[i].equals(varName)) || (!this.caseSensitive && this.name[i].equalsIgnoreCase(varName))) {
                this.value[i] = val;
                return;
            }
        }
        if (this.numVars == this.name.length) {
            String[] tmp1 = new String[2 * this.numVars];
            double[] tmp2 = new double[tmp1.length];
            for (int i2 = 0; i2 < this.numVars; i2++) {
                tmp1[i2] = this.name[i2];
                tmp2[i2] = this.value[i2];
            }
            this.name = tmp1;
            this.value = tmp2;
        }
        this.name[this.numVars] = varName;
        this.value[this.numVars] = val;
        this.numVars++;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public String[] getVariableNames() {
        String[] arr = new String[this.numVars];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = this.name[i];
        }
        return arr;
    }

    public double[] getValues() {
        double[] arr = new double[this.numVars];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = this.value[i];
        }
        return arr;
    }

    public void remove(String varName) {
        for (int i = 0; i < this.numVars; i++) {
            if ((this.caseSensitive && this.name[i].equals(varName)) || (!this.caseSensitive && this.name[i].equalsIgnoreCase(varName))) {
                for (int j = i + 1; j < this.numVars; j++) {
                    this.name[j - 1] = this.name[j];
                    this.value[j - 1] = this.value[j];
                }
                this.numVars--;
                this.name[this.numVars] = null;
                this.value[this.numVars] = 0.0d;
                return;
            }
        }
    }
}
