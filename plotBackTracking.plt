set terminal png size 1000,400 enhanced font "Helvetica,10"
set output 'Programmation par contraintes - Back Tracking.png'
set style data linespoints
set key top left
set xrange [4:30]
set yrange [:4000]
set ylabel "Temps d'execution en ms"
set xlabel "Nombre de reine"
set title "Programmation par contraintes - Back Tracking - DALLE Jean-Baptiste"
plot  "moteurResolution.dat" using 1:3 title 'Back Tracking / First Find', \
      "moteurResolution.dat" using 1:4 title 'Back Tracking / Min First', \
      "moteurResolution.dat" using 1:5 title 'Back Tracking / Max First', \
      "moteurResolution.dat" using 1:6 title 'Back Tracking / Min Domain First', \
      "moteurResolution.dat" using 1:7 title 'Back Tracking / Random', \
      "moteurResolution.dat" using 1:8 title 'AC+Back Tracking / First Find', \
      "moteurResolution.dat" using 1:9 title 'AC+Back Tracking / Min First', \
      "moteurResolution.dat" using 1:10 title 'AC+Back Tracking / Max First', \
      "moteurResolution.dat" using 1:11 title 'AC+Back Tracking / Min Domain First', \
      "moteurResolution.dat" using 1:12 title 'AC+Back Tracking / Random';
