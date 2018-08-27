
#include "real.h"
#include "complex.h"

__kernel void get_landscape(__global struct Token* tokens, __global struct Complex* stack, int token_count, float fminRe, float fminIm, float fdiffRe, float fdiffIm, int width, int height, __global struct ARGB* colours, int area, int stackmax) 
{
    struct Complex z;

    //Get index for this thread
    int i = get_global_id(0) + get_global_id(1) * width;

    //If index is out of bounds, stop this thread
    if (i >= area)
        return;

    //Cast all floats to real (either float or double, depending on support)
    real minRe = (real)fminRe;
    real minIm = (real)fminIm;
    real diffRe = (real)fdiffRe;
    real diffIm = (real)fdiffIm;
    
    //Get value of z for this thread
    z.re = minRe + diffRe / width * get_global_id(0);
    z.im = minIm + diffIm / height * get_global_id(1);

    //Return the colour
    colours[i] = c_colour(evaluate(tokens, stack, token_count, area, i, z));
}