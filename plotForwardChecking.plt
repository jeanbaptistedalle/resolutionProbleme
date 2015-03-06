set terminal png size 1000,400 enhanced font "Helvetica,10"
set output 'Programmation par contraintes - Forward Checking.png'
set style data linespoints
set key top left
set xrange [4:29]
set ylabel "Temps d'execution en ms"
set xlabel "Nombre de reine"
set title "Programmation par contraintes - Forward Checking - DALLE Jean-Baptiste"
plot  "moteurResolution.dat" using 1:11 title 'Forward Checking / First Find', \
      "moteurResolution.dat" using 1:12 title 'Forward Checking / Min First', \
      "moteurResolution.dat" using 1:13 title 'Forward Checking / Max First', \
      "moteurResolution.dat" using 1:14 title 'Forward Checking / Random', \
      "moteurResolution.dat" using 1:15 title 'AC+Forward Checking / First Find',\
      "moteurResolution.dat" using 1:16 title 'AC+Forward Checking / Min First',\
      "moteurResolution.dat" using 1:17 title 'AC+Forward Checking / Max First',\
      "moteurResolution.dat" using 1:18 title 'AC+Forward Checking / Random';