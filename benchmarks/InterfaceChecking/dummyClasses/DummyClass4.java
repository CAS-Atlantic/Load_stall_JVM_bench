package InterfaceChecking.dummyClasses;

import InterfaceChecking.dummyInterfaces.DummyInterface1;
import InterfaceChecking.dummyInterfaces.DummyInterface2;
import InterfaceChecking.dummyInterfaces.DummyInterface3;
import InterfaceChecking.dummyInterfaces.DummyInterface4;

public class DummyClass4
        implements DummyInterface3, DummyInterface2, DummyInterface1, DummyInterface4 {

    private int arg1;
    private int arg2;
    private int arg3;
    private int arg4;
    private int arg5;

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public void setArg3(int arg3) {
        this.arg3 = arg3;
    }

    public void setArg4(int arg4) {
        this.arg4 = arg4;
    }

    public void setArg5(int arg5) {
        this.arg5 = arg5;
    }

    public DummyClass4(int arg1, int arg2, int arg3, int arg4, int arg5) {
        super();
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg5 = arg5;
    }

    @Override
    public int DummyMethod4() {
        int seed1 = arg1;
        int seed2 = arg2;
        int seed3 = arg3;
        int seed4 = arg4;
        int seed5 = arg5;
        int temp1 = 0, temp2 = 0, temp3 = 0, temp4 = 0, temp5 = 0;

        seed1 ^= 0b0000;
        seed2 ^= 0b0001;
        seed3 ^= 0b0010;
        seed4 ^= 0b0011;
        seed5 ^= 0b0100;
        seed1 ^= seed2;
        seed2 ^= seed3;
        seed3 ^= seed4;
        seed4 ^= seed5;
        seed5 ^= seed1;

        temp1 = seed2;
        temp2 = seed3;
        temp3 = seed1;
        temp4 = seed5;
        temp5 = seed4;
        seed1 = temp1;
        seed2 = temp2;
        seed3 = temp3;
        seed4 = temp4;
        seed5 = temp5;

        seed1 ^= 0b0101;
        seed2 ^= 0b0110;
        seed3 ^= 0b0111;
        seed4 ^= 0b1000;
        seed5 ^= 0b1001;
        seed1 ^= seed2;
        seed2 ^= seed4;
        seed3 ^= seed5;
        seed4 ^= seed3;
        seed5 ^= seed1;
        return seed1 ^ seed2 ^ seed3 ^ seed4 ^ seed5;
    }

    @Override
    public int DummyMethod3() {
        int seed1 = arg1;
        int seed2 = arg2;
        int seed3 = arg3;
        int seed4 = arg4;
        int seed5 = arg5;
        int temp1 = 0, temp2 = 0, temp3 = 0, temp4 = 0, temp5 = 0;

        seed1 ^= 0b0001;
        seed2 ^= 0b0010;
        seed3 ^= 0b0011;
        seed4 ^= 0b0100;
        seed5 ^= 0b0101;
        seed1 ^= seed4;
        seed2 ^= seed3;
        seed3 ^= seed5;
        seed4 ^= seed1;
        seed5 ^= seed2;

        temp1 = seed3;
        temp2 = seed1;
        temp3 = seed5;
        temp4 = seed4;
        temp5 = seed2;
        seed1 = temp1;
        seed2 = temp2;
        seed3 = temp3;
        seed4 = temp4;
        seed5 = temp5;

        seed1 ^= 0b0110;
        seed2 ^= 0b0111;
        seed3 ^= 0b1000;
        seed4 ^= 0b1001;
        seed5 ^= 0b1010;
        seed1 ^= seed4;
        seed2 ^= seed5;
        seed3 ^= seed1;
        seed4 ^= seed3;
        seed5 ^= seed2;

        return seed1 ^ seed2 ^ seed3 ^ seed4 ^ seed5;
    }

    @Override
    public int DummyMethod2() {
        int seed1 = arg1;
        int seed2 = arg2;
        int seed3 = arg3;
        int seed4 = arg4;
        int seed5 = arg5;
        int temp1 = 0, temp2 = 0, temp3 = 0, temp4 = 0, temp5 = 0;

        seed1 ^= 0b0010;
        seed2 ^= 0b0011;
        seed3 ^= 0b0100;
        seed4 ^= 0b0101;
        seed5 ^= 0b0110;
        seed1 ^= seed4;
        seed2 ^= seed5;
        seed3 ^= seed1;
        seed4 ^= seed2;
        seed5 ^= seed3;

        temp1 = seed5;
        temp2 = seed4;
        temp3 = seed2;
        temp4 = seed3;
        temp5 = seed1;
        seed1 = temp1;
        seed2 = temp2;
        seed3 = temp3;
        seed4 = temp4;
        seed5 = temp5;

        seed1 ^= 0b0111;
        seed2 ^= 0b1000;
        seed3 ^= 0b1001;
        seed4 ^= 0b1010;
        seed5 ^= 0b1011;
        seed1 ^= seed3;
        seed2 ^= seed1;
        seed3 ^= seed2;
        seed4 ^= seed5;
        seed5 ^= seed4;

        return seed1 ^ seed2 ^ seed3 ^ seed4 ^ seed5;
    }

    @Override
    public int DummyMethod1() {
        int seed1 = arg1;
        int seed2 = arg2;
        int seed3 = arg3;
        int seed4 = arg4;
        int seed5 = arg5;
        int temp1 = 0, temp2 = 0, temp3 = 0, temp4 = 0, temp5 = 0;

        seed1 ^= 0b0011;
        seed2 ^= 0b0100;
        seed3 ^= 0b0101;
        seed4 ^= 0b0110;
        seed5 ^= 0b0111;
        seed1 ^= seed2;
        seed2 ^= seed3;
        seed3 ^= seed4;
        seed4 ^= seed5;
        seed5 ^= seed1;

        temp1 = seed1;
        temp2 = seed4;
        temp3 = seed5;
        temp4 = seed3;
        temp5 = seed2;
        seed1 = temp1;
        seed2 = temp2;
        seed3 = temp3;
        seed4 = temp4;
        seed5 = temp5;

        seed1 ^= 0b1000;
        seed2 ^= 0b1001;
        seed3 ^= 0b1010;
        seed4 ^= 0b1011;
        seed5 ^= 0b1100;
        seed1 ^= seed3;
        seed2 ^= seed4;
        seed3 ^= seed2;
        seed4 ^= seed5;
        seed5 ^= seed1;

        return seed1 ^ seed2 ^ seed3 ^ seed4 ^ seed5;
    }

}
