
#include "real.h"
#include "complex.h"

__kernel void get_landscape(float fminRe, float fminIm, float fdiffRe, float fdiffIm, int width, int height, __global struct ARGB* colours, int area, __local struct Complex* stack) 
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

    //Return the colour
    colours[i] = c_colour(evaluate(NULL, stack, z));
}