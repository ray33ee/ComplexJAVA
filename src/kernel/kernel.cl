
#include "real.h"
#include "complex.h"

__kernel void get_landscape(__global struct Token* tokens, int token_count, float fminRe, float fminIm, float fdiffRe, float fdiffIm, int width, int height, __global struct ARGB* colours, int area, int stackmax, __global struct Complex* stack) 
{
    struct Complex z;

    //Get index for this thread
    int i = get_global_id(0);

    //If index is out of bounds, stop this thread
    if (i >= area)
        return;

    //Cast all floats to real (either float or double, depending on support)
    real minRe = (real)fminRe;
    real minIm = (real)fminIm;
    real diffRe = (real)fdiffRe;
    real diffIm = (real)fdiffIm;
    
    //Get value of z for this thread
    z.re = minRe + diffRe / width * (i % width);
    z.im = minIm + diffIm / height * (i / width);

    int pointer = i;
    
    //variable op constant
    for (int cnt = 0; cnt < token_count; ++cnt)
    {
        int ty = (int)tokens[cnt].type;
        switch ((int)tokens[cnt].type)
        {
            case 1:
                stack[pointer] = z;
                pointer += area;
                break;
            case 2:
                switch ((int)tokens[cnt].re)
                {
                    case 0:
                        pointer -= area;
                        stack[pointer-area] = c_add(stack[pointer-area], stack[pointer]);
                        break;
                    case 1:
                        pointer -= area;
                        stack[pointer-area] = c_sub(stack[pointer-area], stack[pointer]);
                        break;
                    case 2:
                        pointer -= area;
                        stack[pointer-area] = c_mul(stack[pointer-area], stack[pointer]);
                        break;
                    case 3:
                        pointer -= area;
                        stack[pointer-area] = c_div(stack[pointer-area], stack[pointer]);
                        break;
                    case 4:
                        pointer -= area;
                        stack[pointer-area] = c_pow(stack[pointer-area], stack[pointer]);
                        break;
                    case 5:
                        pointer -= area;
                        stack[pointer-area] = c_log(stack[pointer-area], stack[pointer]);
                        break;
                    case 6:
                        stack[pointer-area] = c_neg(stack[pointer-area]);
                        break;
                    case 7:
                        stack[pointer-area] = c_conj(stack[pointer-area]);
                        break;
                    case 8:
                        stack[pointer-area] = c_sqrt(stack[pointer-area]);
                        break;
                    case 9:
                        stack[pointer-area] = c_ln(stack[pointer-area]);
                        break;
                    case 10:
                        stack[pointer-area] = c_exp(stack[pointer-area]);
                        break;
                    case 11:
                        stack[pointer-area] = c_sinh(stack[pointer-area]);
                        break;
                    case 12:
                        stack[pointer-area] = c_cosh(stack[pointer-area]);
                        break;
                    case 13:
                        stack[pointer-area] = c_tanh(stack[pointer-area]);
                        break;
                    case 14:
                        stack[pointer-area] = c_sin(stack[pointer-area]);
                        break;
                    case 15:
                        stack[pointer-area] = c_cos(stack[pointer-area]);
                        break;
                    case 16:
                        stack[pointer-area] = c_tan(stack[pointer-area]);
                        break;
                    case 17:
                        stack[pointer-area] = c_asinh(stack[pointer-area]);
                        break;
                    case 18:
                        stack[pointer-area] = c_acosh(stack[pointer-area]);
                        break;
                    case 19:
                        stack[pointer-area] = c_atanh(stack[pointer-area]);
                        break;
                    case 20:
                        stack[pointer-area] = c_asin(stack[pointer-area]);
                        break;
                    case 21:
                        stack[pointer-area] = c_acos(stack[pointer-area]);
                        break;
                    case 22:
                        stack[pointer-area] = c_atan(stack[pointer-area]);
                        break;
                    case 23:
                        stack[pointer-area] = c_complexr(c_abs(stack[pointer-area]));
                        break;
                    case 24:
                        stack[pointer-area] = c_complexr(c_arg(stack[pointer-area]));
                        break;
                }
                break;
            case 3:
                stack[pointer].re = (real)tokens[cnt].re;
                stack[pointer].im = (real)tokens[cnt].im;
                pointer += area;
                break;
            }
    }


    //Return the colour
    //colours[i] = *(struct ARGB*)&val; 
    colours[i] = c_colour(stack[pointer-area]);
}