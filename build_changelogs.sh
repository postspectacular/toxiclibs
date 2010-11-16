hg log --style changelog -I 'glob:src.core/**' > readme1st/core/CHANGELOG.log
hg log --style changelog -I 'glob:src.audio/**' > readme1st/audio/CHANGELOG.log
hg log --style changelog -I 'glob:src.color/**' > readme1st/color/CHANGELOG.log
hg log --style changelog -I 'glob:src.data/**' > readme1st/data/CHANGELOG.log
hg log --style changelog -I 'glob:src.image/**' > readme1st/image/CHANGELOG.log
hg log --style changelog -I 'glob:src.p5/**' > readme1st/p5/CHANGELOG.log
hg log --style changelog -I 'glob:src.physics/**' > readme1st/physics/CHANGELOG.log
hg log --style changelog -I 'glob:src.sim/**' > readme1st/sim/CHANGELOG.log
hg log --style changelog -I 'glob:src.volume/**' > readme1st/volume/CHANGELOG.log
echo "done."
