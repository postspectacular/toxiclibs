hg log --style changelog -I 'glob:src.core/**' > readme1st/core/CHANGELOG.txt
hg log --style changelog -I 'glob:src.audio/**' > readme1st/audio/CHANGELOG.txt
hg log --style changelog -I 'glob:src.color/**' > readme1st/color/CHANGELOG.txt
hg log --style changelog -I 'glob:src.data/**' > readme1st/data/CHANGELOG.txt
hg log --style changelog -I 'glob:src.image/**' > readme1st/image/CHANGELOG.txt
hg log --style changelog -I 'glob:src.physics/**' > readme1st/physics/CHANGELOG.txt
hg log --style changelog -I 'glob:src.sim/**' > readme1st/sim/CHANGELOG.txt
hg log --style changelog -I 'glob:src.volume/**' > readme1st/volume/CHANGELOG.txt
echo "done."
