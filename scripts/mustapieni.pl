#open PUTKI, "| convert -depth 8 -size 64x64 rgb:- kuva.png";
open FAILI, ">mustapieni.rgb";
for(1..256){
  for(1..256){
    print FAILI chr(0);
    print FAILI chr(0);
    print FAILI chr(0);
  }
}
close PUTKI;
close FAILI;
