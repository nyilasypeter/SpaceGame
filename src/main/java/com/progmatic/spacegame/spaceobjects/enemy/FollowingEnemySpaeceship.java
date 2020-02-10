package com.progmatic.spacegame.spaceobjects.enemy;

import com.progmatic.spacegame.SpaceObjectProvider;
import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.events.SpaceshipMotionListener;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import com.progmatic.spacegame.spaceobjects.Spaceship;
import com.progmatic.spacegame.spaceobjects.gifts.Gift;
import com.progmatic.spacegame.spaceobjects.gifts.Gold;
import com.progmatic.spacegame.spaceobjects.gifts.Life;
import com.progmatic.spacegame.spaceobjects.gifts.MissilePack;
import com.progmatic.spacegame.spaceobjects.projectile.Projectile;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class FollowingEnemySpaeceship extends EnemySpaceship implements SpaceshipMotionListener {

    private enum MyState{
        ENTER, FOLLOW, FIRE, KAMIKAZE, LEAVE
    }

    private MyState myState = MyState.ENTER;
    private Spaceship spaceship;
    private static final int SHOULD_LEAVE = 1000;
    private int startFollowAtDepth;
    private int startToLeaveAt;
    private Random random = new Random();
    private long stateChangedAt;
    private Rectangle spaceShipLocation;
    private boolean shouldAlwaysFire;

    public FollowingEnemySpaeceship(Rectangle spaceShipLocation){
        super();
        SPACESIP_COLOR_TOP = Color.decode("#F134DB");
        startFollowAtDepth = random.nextInt(300) + 50;
        startToLeaveAt = random.nextInt(8) + 5;
        this.spaceShipLocation = spaceShipLocation;
        this.shouldAlwaysFire = random.nextBoolean();
        if(shouldAlwaysFire){
            SPACESIP_COLOR_BOTTOM = Color.decode(("#F9FC4F"));
        }
        this.fireFrequency = random.nextInt(15) + 10;
        this.nrOfProjectiles = random.nextInt(3)+1;
    }


    @Override
    public void move() {
        setState();
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            if(MyState.ENTER.equals(myState) || MyState.LEAVE.equals(myState)){
                absCent.x = absCent.x - speed;
                setBoundsAroundCenter(absCent, getComponentWidth(), getComponentHeight());
            }
            else if(MyState.FOLLOW.equals(myState)){
                follow();
            }

        } else if (this.state.equals(SpaceObjectState.AGOZNIZING)) {
            repeatNr++;
            if (repeatNr >= maxRepeatNr) {
                state = SpaceObjectState.DEAD;
            }
        }
    }

    private void follow() {
        if(spaceShipLocation.y < getBounds().y){
            absCent.y = absCent.y - speed;
        }
        else{
            absCent.y = absCent.y + speed;
        }
        setBoundsAroundCenter(absCent, getComponentWidth(), getComponentHeight());
    }

    private void setState() {
        if(MyState.ENTER.equals(myState) && isInGameField(startFollowAtDepth)){
            stateChangedAt = System.currentTimeMillis();
            myState = MyState.FOLLOW;
        }
        if(!MyState.ENTER.equals(myState)){
            if(since() > startToLeaveAt){
                stateChangedAt = System.currentTimeMillis();
                myState = MyState.LEAVE;
            }
        }
    }

    private int since(){
        long now = System.currentTimeMillis();
        long duration = now - stateChangedAt;
        int ret =   (int)duration / 1000;
        return ret;
    }

    private boolean isInGameField(int depth){
        int fullWidth = SpaceObjectProvider.instance().getSizeOfGameField().width;
        Rectangle myBounds = getBounds();
        return myBounds.x < fullWidth - getComponentWidth() - depth;
    }

    @Override
    public List<Projectile> getProjectiles() {
        if(shouldAlwaysFire){
            return super.getProjectiles();
        }
        else if(MyState.FIRE.equals(myState)) {
            return super.getProjectiles();
        }
        else{
            return null;
        }
    }

    @Override
    public void spaceshipMoved(Rectangle newBounds) {
        spaceShipLocation = newBounds;
        if(!MyState.ENTER.equals(myState) && !MyState.LEAVE.equals(myState)){
            if(closeEnoughToFire(newBounds)){
                myState = MyState.FIRE;
            }
            else{
                myState = MyState.FOLLOW;
            }
        }
    }

    private boolean closeEnoughToFire(Rectangle spaceShipLocation){
        Rectangle myBounds = getBounds();
        return (Math.abs(myBounds.y - spaceShipLocation.y)<200);
    }

    @Override
    public SpaceObject createGiftAfterDying() {
        Gift gift;
        int rand = random.nextInt(3);
        switch (rand){
            case 0: gift = new Gold(400);
            break;
            case 1: gift = new Life(5);
            break;
            case 2: gift = new MissilePack(10);
            break;
            default:throw new RuntimeException("Default should not be reached here");
        }
        Point center = getAbsoluteCenter();
        gift.setBounds(
                center.x - gift.getComponentWidth() / 2,
                center.y - gift.getComponentHeight() / 2,
                gift.getComponentWidth(),
                gift.getComponentHeight());
        return gift;
    }
}
