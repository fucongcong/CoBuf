<?php 
function zigZagEncode32($int32)
{
  if (PHP_INT_SIZE == 8) {
      $trim_int32 = $int32 & 0xFFFFFFFF;
      return (($trim_int32 << 1) ^ ($int32 << 32 >> 63)) & 0xFFFFFFFF;
  } else {
      return ($int32 << 1) ^ ($int32 >> 31);
  }
}

function zigZagDecode32($uint32)
{
    // Fill high 32 bits.
    if (PHP_INT_SIZE === 8) {
        $uint32 |= ($uint32 & 0xFFFFFFFF);
    }

    $int32 = (($uint32 >> 1) & 0x7FFFFFFF) ^ (-($uint32 & 1));

    return $int32;
}

$int = 10;
$code = zigZagEncode32($int);
var_dump($code);
$decode = zigZagDecode32($code);
var_dump($decode);