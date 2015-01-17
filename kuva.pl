open PUTKI, "| convert -depth 8 -size 64x64 rgb:- kuva.png";
open FAILI, ">kuva.rgb";
for(1..64){
  for(1..64){
    print FAILI chr(255);
    print FAILI chr(0);
    print FAILI chr(255);
  }
}
close PUTKI;
close FAILI;
