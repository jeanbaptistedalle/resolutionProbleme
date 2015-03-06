set terminal png size 1000,400 enhanced font "Helvetica,10"
set output 'Recherche Locale.png'
set style data linespoints
set key top left
set ylabel "Temps d'execution en ms"
set xlabel "Nombre de reine"
set xrange [4:]
set title "Recherche locale - DALLE Jean-Baptiste"
plot "moteurResolution.dat" using 1:2 title 'Recherche locale';
