package javagame;


import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Random;

public class NPC extends NPCMovement {

        // Max Health
    private double MAX_HEALTH;
        // Max Stamina
    private double MAX_STAMINA;

        // Base Attack and Defend
    private int BASE_ATTACK ;
    private int BASE_DEFENCE;

        // overall attack and defence
    private int OVERALL_ATTACK;
    private int OVERALL_DEFENCE;

        // Attack Stamina and Damage
    private int attackStamina;
    private int attackDamage;

        // Player Health
    private double health;
        // Player Stamina
    private double stamina;
        // Player level
    private int npcLevel;

        // NPC's Inventory
    private Inventory inventory;

        // How long arrow will live for.
    private int FIRE_RATE = 250;
        // Index of the projectile array
    private int currentIndex = 0;
        // Time since last shot
    private int lastShot = 0;

        // If the player is in combat
    private boolean inCombat;

        // If NPC is alive;
    private boolean isAlive;

        // If should bge rendered
    private boolean willRender;

    private boolean good;

        // This is what skin the character has
    private int npcRace;

        // This is the class that the npc is
        // 0=hunter 1=warrior 2=wizard 3=rogue
    private int npcClass;

    private Image[] projectileImage;

    private Color red;
    private Image healthBar;
        /*
        The player class is necessary to determine the players
        location for attacking/rendering/level/location
         */
    private Player player;

    public NPC () {
        super();
            // Set player to be dead
        this.isAlive = false;
        this.willRender = false;
    }

        // NPC Enemy with race
    public NPC( int race  ) {
        super();

            // Set the race
        this.npcRace = race;
            // Get player class
        this.player = this.player.getInstance();

        Random random = new Random();
        int playerLevel = this.player.getLevel();

        // Sets NPC level from a range of two above and two below.
        this.npcLevel = random.nextInt(  ( ( playerLevel + 2 ) - ( playerLevel - 2 ) ) + 1 ) + ( playerLevel - 2 );

        if( this.npcLevel <= 0 ) {
            this.npcLevel = 1;
        }

        // Setting the max health and stamina based on level
        this.MAX_HEALTH = 90 + ( this.npcLevel * 5 );
        this.MAX_STAMINA = 90 + ( this.npcLevel * 5 );

        // Setting the current npc health and stamina
        this.health = this.MAX_HEALTH;
        this.stamina = this.MAX_STAMINA;

            // Setting the class randomly
        this.npcClass = random.nextInt( 4 );
            // Setting stats and image
        try {
            setClassStat();
            setImages( race );
        }
        catch ( SlickException e ) {
            e.printStackTrace();
        }

            // Setting up the NPC Inventory
        this.inventory = new Inventory();
        this.inventory.setClassID( this.npcClass );
        this.inventory.setBaseAttack( this.BASE_ATTACK );
        this.inventory.setBaseDefence(this.BASE_DEFENCE);
        this.inventory.addEnemyNPCArmor( this.npcLevel );

            // Setting attack and defence
        this.OVERALL_ATTACK = this.inventory.getPlayerOverallAttack();
        this.OVERALL_DEFENCE = this.inventory.getPlayerOverallDefence();

        good = false;

        setNPCClass( "fancyOrk.png", 3 );

        this.isAlive = true;
        this.willRender = false;

        setNPCX( 93 );
        setNPCY( 96 );
    }

        // NPC Enemy with race and class
    public NPC( int race, int npcClass  ) {

        super();
            // Set the race
        this.npcRace = race;

            // Get player class
        this.player = this.player.getInstance();

        Random random = new Random();
        int playerLevel = this.player.getLevel();

            // Sets NPC level from a range of two above and two below.
        this.npcLevel = random.nextInt(  ( ( playerLevel + 2 ) - ( playerLevel - 2 ) ) + 1 ) + ( playerLevel - 2 );

        if( this.npcLevel <= 0 ) {
            this.npcLevel = 1;
        }

            // Setting the max health and stamina based on level
        this.MAX_HEALTH = 90 + ( this.npcLevel * 5 );
        this.MAX_STAMINA = 90 + ( this.npcLevel * 5 );

            // Setting the current npc health and stamina
        this.health = this.MAX_HEALTH;
        this.stamina = this.MAX_STAMINA;

        this.npcClass = npcClass;


            // Setting stats and image
        try {
            setClassStat();
            setImages( race );
        }
        catch ( SlickException e ) {
            e.printStackTrace();
        }

            // Setting up the NPC Inventory
        this.inventory = new Inventory();
        this.inventory.setClassID( npcClass );
        this.inventory.setBaseAttack( this.BASE_ATTACK );
        this.inventory.setBaseDefence(this.BASE_DEFENCE);
        this.inventory.addEnemyNPCArmor( this.npcLevel );


            // Setting attack and defence
        this.OVERALL_ATTACK = this.inventory.getPlayerOverallAttack();
        this.OVERALL_DEFENCE = this.inventory.getPlayerOverallDefence();

        this.good = false;

        setNPCClass( "fancyOrk.png", 3 );

        this.isAlive = true;
        this.willRender = false;

        setNPCX( 93 );
        setNPCY( 96 );

    }
        // Class set stats
    private void setClassStat() throws SlickException{
        switch ( this.npcClass ) {
                // Hunter
            case 0:
                projectileImage = new Image[4];
                projectileImage[0] = new Image("NewEra-Beta/res/projectiles/Arrow-Up.png");
                projectileImage[1] = new Image("NewEra-Beta/res/projectiles/Arrow-Right.png");
                projectileImage[2] = new Image("NewEra-Beta/res/projectiles/Arrow-Down.png");
                projectileImage[3] = new Image("NewEra-Beta/res/projectiles/Arrow-Left.png");

                this.BASE_ATTACK = 10;
                this.BASE_DEFENCE = 3;

                break;
                // Warrior
            case 1:
                this.BASE_ATTACK = 7;
                this.BASE_DEFENCE = 10;

                break;
                // Wizard
            case 2:
                projectileImage = new Image[4];
                projectileImage[0] = new Image("NewEra-Beta/res/projectiles/FireBall-Up.png");
                projectileImage[1] = new Image("NewEra-Beta/res/projectiles/FireBall-Right.png");
                projectileImage[2] = new Image("NewEra-Beta/res/projectiles/FireBall-Down.png");
                projectileImage[3] = new Image("NewEra-Beta/res/projectiles/FireBall-Left.png");

                this.BASE_ATTACK = 13;
                this.BASE_DEFENCE = 2;

                break;
                // Rouge
            case 3:
                this.BASE_ATTACK = 9;
                this.BASE_DEFENCE = 7;
        }
    }

    public boolean getIsAlive() { return this.isAlive; }

    private void setImages( int race ) throws SlickException {

        this.healthBar = new Image( "NewEra-Beta/res/enemies/EnemyHealth.png" );
        this.red = new Color( 225, 0, 0, .7f );
    }
        // Inventory
    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory( Inventory inventory) {
        this.inventory = inventory;
    }

        // Combat
    public boolean getInCombat() { return this.inCombat; }
    public void setInCombat( boolean inCombat ) { this.inCombat = inCombat; }

        // Max health and stamina
    public double getMaxHealth() { return this.MAX_HEALTH; }
    public double getMaxStamina() { return this.MAX_STAMINA; }

        // Attack and defence
    public int getOverallAttack() { return this.OVERALL_ATTACK; }
    public int getOverallDefence() { return this.OVERALL_DEFENCE; }

        // Base attack and defence
    public int getBaseAttack() { return this.BASE_ATTACK; }
    public int getBaseDefence() { return this.BASE_DEFENCE; }

        // Health
    public double getHealth() {
        return this.health;
    }
    public void decreaseHealth( double damage ) {
        this.health -= damage;
    }
    public boolean checkDeath() {
        if( getHealth() <= 0 ) {
            this.health = 0;
            this.isAlive = false;
            return true;
        }
        return false;
    }

        // Stamina
    public double getStamina() {
        return this.stamina;
    }
    public void decreaseStamina( ) { this.stamina -= 10;  }

        // Exp
    public int getExp() {
        Random rand = new Random();
        return ( ( this.npcLevel + 3 ) * ( rand.nextInt( 3 ) + 1 ) );
    }

        // Used Potion
    public void usedHealthPotion() {
        if( this.inventory.getHealthPotions() > 0 ) {
            this.health += 30;
            this.inventory.useHealthPotion();
        }
    }
    public void usedStaminaPotion() {
        if( this.inventory.getStaminaPotions() > 0 ) {
            this.stamina += 30;
            this.inventory.useStaminaPotion();
        }
    }

  /*  public void drawPlayer( Graphics g ) {
        double healthPercent = getHealth() / MAX_HEALTH;

        g.setColor( red );
    }
*/
    public void takeDamage() {
        Random rand = new Random();
        double test ;
        test = ( ( (this.player.getOverallAttack() * rand.nextInt( 20 ) + 1) * 30  ) / ( this.OVERALL_DEFENCE ) )+ player.getAttackOfCurrentAttack();

        System.out.println( "Damage: " + (int)test + ", Defence: " + this.getOverallDefence() );

        this.health -= (int)test;


         if ( checkDeath() ) {
             startAnimationDeath();
             stopAnimationDeath();
         }


    }

    public void drawNPC( Graphics g ) {

        int npcX = (int)getNPCX();
        int npcY = (int)getNPCY();
        int playerX = (int)this.player.getPlayerX();
        int playerY = (int)this.player.getPlayerY();
        int x, y;

        x = npcX - playerX;
        y = npcY - playerY;

        if( ( ( 352 + x ) >= 0 && (352 + x ) <= 672 ) &&
                ( ( 352 + y ) >= 0 && (352 + y ) <= 672 ) ) {

            double healthPercent = getHealth() / MAX_HEALTH;
            g.setColor( red );

            g.fillRect(328 + x, 319 + y, 16 * (float)healthPercent, 6  );
            this.healthBar.draw( 328+x, 319+y );

            if( this.isAlive ) {
                getMovingNPC().draw( 320 + x, 320 + y);
            }
            else {
                getDieingNPC().draw( 320 + x, 320 + y);
            }

        }
    }
   // public void drawPlayerDieing( float x, float y ) { npcDeath.draw( x, y );  }
}

