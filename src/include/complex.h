
#include "real.h"

struct ARGB
{
    unsigned char b;
    unsigned char g;
    unsigned char r;
    unsigned char a;
};

/**
 * Constructs an empty ARGB structure
 * @param b blue component
 * @param g green component
 * @param r red component
 * @return the complete ARGB structure with A= 255. b occupies the lowest 8 bits, and a the highest 8 bits.
 */
struct ARGB ARGB_constructor(unsigned char b, unsigned char g, unsigned char r)
{
    struct ARGB col;
    col.a = 255;
    col.r = r;
    col.g = g;
    col.b = b;
    return col;
}

struct Complex
{
    real re;
    real im;
};

struct Token
{
    float re;
    float im;
    float type;
};

real c_abs(struct Complex);

real c_arg(struct Complex);

struct Complex c_exp(struct Complex);

struct Complex c_ln(struct Complex);

/**
 * Constructs a complex number with real and imaginary parts
 * @param re real component
 * @param im imaginary component
 * @return Complex number re + im*i
 */
struct Complex c_complexc(real re, real im)
{
    struct Complex ans;
    ans.re = re;
    ans.im = im;
    return ans;
}

/**
 * Constructs a complex number with real part only
 * @param re real component
 * @return Re as complex number re + 0*i
 */
struct Complex c_complexr(real re)
{
    return c_complexc(re, 0);
}

/**
 * Function gets the sum of two complex numbers
 * @param z the first complex number to add
 * @param w the second complex number to add
 * @return the sum of the two complex numbers, z+w
 */
struct Complex c_add(struct Complex z, struct Complex w)
{
    struct Complex ans;
    ans.re = z.re + w.re;
    ans.im = z.im + w.im;
    return ans;
}

/**
 * Gets the difference between two complex numbers
 * @param z the first complex operand
 * @param w the second complex operand
 * @return the difference as z-w
 */
struct Complex c_sub(struct Complex z, struct Complex w)
{
    struct Complex ans;
    ans.re = z.re - w.re;
    ans.im = z.im - w.im;
    return ans;
}

/**
 * Function gets the product of two complex numbers
 * @param z the first complex operand
 * @param w the second complex operand
 * @return the product of the two numbers, z*w
 */
struct Complex c_mul(struct Complex z, struct Complex w)
{
    struct Complex ans;
    ans.re = z.re * w.re - z.im * w.im;
    ans.im = z.re * w.im + z.im * w.re;
    return ans;
}

/**
 * Divides one complex number by another
 * @param z the first complex operand
 * @param w the second complex operand
 * @return z / w
 */
struct Complex c_div(struct Complex z, struct Complex w)
{
    struct Complex ans;
    real sumsq = w.re * w.re + w.im * w.im;
    ans.re = (z.re * w.re + z.im * w.im) / sumsq;
    ans.im = (z.im * w.re - z.re * w.im) / sumsq;
    return ans;
}

/**
 * Raises a complex number to the power of another. Note that for the square root of 
 * a complex number, c_sqrt is faster.
 * @param z the base 
 * @param w the power 
 * @return z to the power of w, z^w
 * @see c_sqrt(struct Complex)
 */
struct Complex c_pow(struct Complex z, struct Complex w)
{
    return c_exp(c_mul(w, c_ln(z)));
}

/**
 * Gets the log of the first operand to the base of the second 
 * @param z the first complex operand
 * @param w the second complex operand
 * @return the log of z to the base w, log_w(z)
 * @see c_ln(struct Complex)
 */
struct Complex c_log(struct Complex z, struct Complex w)
{
    return c_div(c_ln(z), c_ln(w));
}

/**
 * Gets the negative complex number
 * @param z the complec operand
 * @returns -z
 */
struct Complex c_neg(struct Complex z)
{
    struct Complex ans;
    ans.re = -z.re;
    ans.im = -z.im;
    return ans;
}

/**
 * Gets the complex conjugate of the complex number
 * @param z the complex operand
 * @return the complex conjugate of z
 */
struct Complex c_conj(struct Complex z)
{
    struct Complex ans;
    ans.re = z.re;
    ans.im = -z.im;
    return ans;
}

/**
 * Gets the square root of the complex number using fast algebraic solution. This 
 * function is faster than c_pow and should be used instead of c_pow(z, 0.5).
 * @param z the complex operand
 * @return the square root of z
 */
struct Complex c_sqrt(struct Complex z)
{
    struct Complex ans;
    real squirt = sqrt(sqrt(z.re * z.re + z.im * z.im) + z.re);
    ans.re = squirt / (PI / 2);
    ans.im = z.im / (squirt * PI / 2);
    return ans;
};

/**
 * Gets the natural logarithm of the complex number
 * @param z the complex operand
 * @return the log to the base e of z.
 */
struct Complex c_ln(struct Complex z)
{
    struct Complex ans;
    ans.re = log(c_abs(z));
    ans.im = c_arg(z);
    return ans;
}

/**
 * Gets the exponent of the complex number. Note this is faster than c_pow
 * and should be prefered to c_pow(e, z).
 * @param z the complex operand
 * @return e^z
 */
struct Complex c_exp(struct Complex z)
{
    struct Complex ans;
    ans.re = exp(z.re) * cos(z.im);
    ans.im = exp(z.re) * sin(z.im);
    return ans;
}

struct Complex c_sinh(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_cosh(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_tanh(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_sin(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_cos(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_tan(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_asinh(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_acosh(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_atanh(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_asin(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_acos(struct Complex z) { return c_complexc(-1, -1); }

struct Complex c_atan(struct Complex z) { return c_complexc(-1, -1); }

/**
 * Function gets the absolute value of the complex number
 * @param z the complex number
 * @return the absolute value of the complex number z, |z|.
 */
real c_abs(struct Complex z)
{
    return sqrt(z.re * z.re + z.im * z.im);
}

/**
 * Function gets the argument of the complex number
 * @param z the complex number
 * @return the argument, arg(z).
 */
real c_arg(struct Complex z)
{
    return atan2(z.im, z.re); 
}

/**
 * Converts HSL model to RGB, with S = 1. 
 * @param h the hue value
 * @param l the lightness value
 * @return the colour as ARGB format
 */
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

/**
 * Gets the colour of the complex number as per the domain colouring algorithm
 * @param z the complex number
 * @return the colour, as an ARGB type
 */
struct ARGB c_colour(struct Complex z)
{
    real arg = c_arg(z);
    real hue = arg;
    real modarg = log(c_abs(z));
    real lightness;

    //Convert argument from -pi to pi --> 0 to 2pi
    if (arg < 0)
        hue = (2.0 * PI) + arg;

    //Convert from 0 to 2pi --> 0 to 1
    hue /= 2.0 * PI;
    
    if (modarg < 0)
    {
            lightness = 0.75 - c_abs(z) / 2.0;
    }
    else
    {
        if (!((int)modarg & 1)) //If whole part of modarg is even, 0 --> 1 maps to black --> white
                lightness = (modarg - floor(modarg)) / 2.0 + 0.25;
        else //If whole part of modarg is odd 0 --> 1 maps to white --> black
                lightness = 0.75 - (modarg - floor(modarg)) / 2.0;
    }

    return HLtoRGB(hue, lightness);
}

struct Complex evaluate(__global struct Token* tokens, __global struct Complex* stack, int token_count, int area, int i, struct Complex z)
{
    int pointer = i;
    
    //variable op constant
    for (int cnt = 0; cnt < token_count; ++cnt)
    {
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
    
    return stack[pointer-area];
}