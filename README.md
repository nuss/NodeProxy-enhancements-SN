# NodeProxy-enhancements
some additional NodeProxy methods

Add a method 'seti' to NodeProxy, allowing to set an arg for a single channel in a multichannel NodeProxy:
```
// 5-channel NodeProxy
(
Ndef(\multichannel, {
  SinOsc.ar(\freq.kr(220!5));
});
)

Ndef(\multichannel).getKeysValues;
// [ [ freq, [ 220, 220, 220, 220, 220 ] ] ] -> freq in all 5 channels set to 220

// set the third channel's freq to 330
Ndef(\multichannel).seti(\freq, 2, 330);

Ndef(\multichannel).getKeysValues;
// [ [ freq, [ 220, 220, 330, 220, 220 ] ] ] -> freq inthird channel set to 330
```
## an additional NodeProxy role
This NodeProxy role allows you to set args for each channel individally with an individual timing
```
(
5.do({ |i|
  // add a role for each source strating at 1 (0 is the Ndef's source itself)
  Ndef(\multichannel)[i+1] = \seti -> Pbind(
    \freq, Pseq(#[220, 240, 330], inf),
    \dur, Prand(#[0.1, 0.3, 0.5], inf),
    \channelOffset, i // the channel to be set, rather an offset than a fixed value
  )
})
)
```
