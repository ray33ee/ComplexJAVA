
#include "real.h"

struct Complex
{
    real re;
    real im;
};

struct ARGB
{
    unsigned char b;
    unsigned char g;
    unsigned char r;
    unsigned char a;
};

struct ARGB ARGB_constructor(unsigned char b, unsigned char g, unsigned char r)
{
    struct ARGB col;
    col.a = 255;
    col.r = r;
    col.g = g;
    col.b = b;
    return col;
}

struct Complex c_addc(struct Complex z, struct Complex w)
{
    struct Complex ans;
    ans.re = z.re + w.re;
    ans.im = z.im + w.im;
    return ans;
}

struct Complex c_addr(struct Complex z, real c)
{
    struct Complex ans;
    ans.re = z.re + c;
    ans.im = z.im;
    return ans;
}

struct Complex c_mulr(struct Complex z, real c)
{
    struct Complex ans;
    ans.re = z.re * c;
    ans.im = z.im * c;
    return ans;
}

struct Complex c_mulc(struct Complex z, struct Complex w)
{
    struct Complex ans;
    ans.re = z.re * w.re - z.im * w.im;
    ans.im = z.re * w.im + z.im * w.re;
    return ans;
}

real c_arg(struct Complex z)
{
    return atan2(z.im, z.re); 
}

struct ARGB HLtoRGB(real h, real l)
{

    real v;

    //As the argument could be pi radians, the hue could be 1.0. As this is an invalid value, this can be fixed by making the hue 0, which (as the hue is cyclic) is the same
    if (h == 1.0)
        h = 0;

    v = (l <= 0.5) ? l * 2 : 1;

    if (v > 0)
    {
        real m;
        real sv;
        int sextant;
        real vsf, mid1, mid2;

        m = 2 * l - v;
        sv = (v - m) / v;
        h *= 6.0;
        sextant = (int)h;
        vsf = v * sv * (h - sextant);
        mid1 = m + vsf;
        mid2 = v - vsf;
        switch (sextant)
        {
        case 0:
                return ARGB_constructor((unsigned char)(mid2 * 255), (unsigned char)(v * 255), (unsigned char)(m * 255))  ;
        case 1:
                return ARGB_constructor((unsigned char)(m * 255), (unsigned char)(v * 255), (unsigned char)(mid1 * 255)) ;
        case 2:
                return ARGB_constructor((unsigned char)(m * 255), (unsigned char)(mid2 * 255), (unsigned char)(v * 255)) ;
        case 3:
                return ARGB_constructor((unsigned char)(mid1 * 255), (unsigned char)(m * 255), (unsigned char)(v * 255)) ;
        case 4:
                return ARGB_constructor((unsigned char)(v * 255), (unsigned char)(m * 255), (unsigned char)(mid2 * 255))  ;
        case 5:
                return ARGB_constructor((unsigned char)(v * 255), (unsigned char)(mid1 * 255), (unsigned char)(m * 255)) ;
        }
    }
    //If the application gets here, there is a problem. Output the otherwise impossible grey to indicate error
    return ARGB_constructor(0,0,255) ;
}

struct ARGB c_colour(struct Complex z)
{
    real arg = c_arg(z);
    real hue = arg;

    //Convert argument from -pi to pi --> 0 to 2pi
    if (arg < 0)
        hue = (2.0 * PI) + arg;

    //Convert from 0 to 2pi --> 0 to 1
    hue /= 2.0 * PI;

    return HLtoRGB(hue, 0.5);
}
