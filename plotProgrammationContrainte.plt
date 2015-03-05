set terminal png size 1000,400 enhanced font "Helvetica,10"
set output 'Programmation par contraintes.png'
set style data linespoints
set ylabel "Temps d'execution en ms"
set xlabel "Nombre de reine"
set title "Programmation par contraintes - DALLE Jean-Baptiste"
plot  "moteurResolution.dat" using 1:3 title 'Back Tracking', \
      "moteurResolution.dat" using 1:4 title 'AC/Back Tracking', \
      "moteurResolution.dat" using 1:5 title 'Forward Checking', \
      "moteurResolution.dat" using 1:6 title 'AC/Forward Checking';
