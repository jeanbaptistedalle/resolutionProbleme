set terminal png size 1000,400 enhanced font "Helvetica,10"
set output 'Programmation par contraintes - Heuristique Random et Min Domain Size.png'
set style data linespoints
set key top left
set xrange [4:100]
set yrange [:4000]
set ylabel "Temps d'execution en ms"
set xlabel "Nombre de reine"
set title "Programmation par contraintes - Random - DALLE Jean-Baptiste"
plot  "moteurResolution.dat" using 1:7 title 'Back Tracking / Random', \
      "moteurResolution.dat" using 1:12 title 'AC+Back Tracking / Random',\
      "moteurResolution.dat" using 1:17 title 'Forward Checking / Random', \
      "moteurResolution.dat" using 1:22 title 'AC+Forward Checking / Random';
