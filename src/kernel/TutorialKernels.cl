
#include "real.h"
#include "complex.h"

__kernel void get_landscape(float fminRe, float fminIm, float fdiffRe, float fdiffIm, int width, int height, __global struct ARGB* colours, int area) 
{
    int i = get_global_id(0);

    if (i >= area)
        return;

    int x = i % width;
    int y = i / width;

    real minRe = (real)fminRe;
    real minIm = (real)fminIm;
    real diffRe = (real)fdiffRe;
    real diffIm = (real)fdiffIm;

    struct Complex z;

    z.re = minRe + diffRe / width * x;
    z.im = minIm + diffIm / height * y;

    struct Complex ans = c_addr(c_addc(c_mulc(z,z), c_mulr(z, 2)), 2);

    colours[i] = c_colour(ans);
}