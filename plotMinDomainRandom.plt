set terminal png size 1000,400 enhanced font "Helvetica,10"
set output 'Programmation_par_contraintes_-_Random_Min_Domain_Size.png'
set style data lines
set key top left
set datafile separator ":"
set xrange [4:90]
set logscale y
set ylabel "Temps d'execution en ms"
set xlabel "Nombre de reine"
set title "Programmation par contraintes - Min domain size first / Random - DALLE_Jean-Baptiste"
plot  "moteurResolution.dat" using 1:16 title 'Forward Checking / Min Domain First', \
      "moteurResolution.dat" using 1:17 title 'Forward Checking / Random', \
      "moteurResolution.dat" using 1:21 title 'AC+Forward Checking / Min Domain First',\
      "moteurResolution.dat" using 1:22 title 'AC+Forward Checking / Random';
