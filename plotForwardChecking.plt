set terminal png size 1000,400 enhanced font "Helvetica,10"
set output 'Programmation_par_contraintes_-_Forward_Checking.png'
set style data lines
set key top left
set datafile separator ":"
set xrange [4:31]
set yrange [:7000]
set ylabel "Temps d'execution en ms"
set xlabel "Nombre de reine"
set title "Programmation par contraintes - Forward Checking - DALLE Jean-Baptiste"
plot  "moteurResolution.dat" using 1:13 title 'Forward Checking / First Find', \
      "moteurResolution.dat" using 1:14 title 'Forward Checking / Min First', \
      "moteurResolution.dat" using 1:15 title 'Forward Checking / Max First', \
      "moteurResolution.dat" using 1:16 title 'Forward Checking / Min Domain First', \
      "moteurResolution.dat" using 1:17 title 'Forward Checking / Random', \
      "moteurResolution.dat" using 1:18 title 'AC+Forward Checking / First Find',\
      "moteurResolution.dat" using 1:19 title 'AC+Forward Checking / Min First',\
      "moteurResolution.dat" using 1:20 title 'AC+Forward Checking / Max First',\
      "moteurResolution.dat" using 1:21 title 'AC+Forward Checking / Min Domain First',\
      "moteurResolution.dat" using 1:22 title 'AC+Forward Checking / Random';
