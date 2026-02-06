for file in *.txt; do
  if [ -e "$file" ]; then
    mv "$file" "OLD_$file"
  fi
done