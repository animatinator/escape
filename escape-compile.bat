javac escape/BodyArmourUpgrade.java
javac escape/Chaser.java
javac escape/CollisionActor.java
javac escape/CollisionObstacle.java
javac escape/Collisions.java
javac escape/Constants.java
javac escape/Container.java
javac escape/Door.java
javac escape/Effect.java
javac escape/Effects.java
javac escape/Enemy.java
javac escape/Examinable.java
javac escape/FlashEffect.java
javac escape/Game.java
javac escape/GameObject.java
javac escape/Inventory.java
javac escape/InventoryDisplay.java
javac escape/Item.java
javac escape/ItemManager.java
javac escape/Keyboard.java
javac escape/LanternUpgrade.java
javac escape/Level.java
javac escape/LevelTile.java
javac escape/LineEffect.java
javac escape/Main.java
javac escape/MessageBox.java
javac escape/MessageDisplay.java
javac escape/MessageSender.java
javac escape/Messages.java
javac escape/Mouse.java
javac escape/Player.java
javac escape/Ray.java
javac escape/RunningShoesUpgrade.java
javac escape/Textures.java
javac escape/Upgrade.java
javac escape/Vector.java
javac escape/Wall.java
javac escape/Weapon.java
javac escape/Zombie.java
jar -cfe WIP.jar escape.Main escape/*.* escape/img/ escape/lvl/

@echo off
echo.
echo.
echo Finished!
echo.

set /p run="Run jar? (y/n): "
if %run%==y java -jar WIP.jar