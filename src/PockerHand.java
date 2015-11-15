/**
 * Created by Вадик on 14.11.2015.
 */


class PokerHand extends Deck {

        public static void main (String args[]){
             int cardsInHand = 7;

                /**  Deck deck = new Deck();
                deck.shuffle();
                PokerHand hand0 = new PokerHand(5);
                PokerHand hand1 = new PokerHand(5);
                PokerHand hand2 = new PokerHand(5);
                PokerHand hand3 = new PokerHand(5);
                hand0.deal(deck, 5);
                hand1.deal(deck, 5);
                hand2.deal(deck, 5);
                hand3.deal(deck, 5);


                hand0.print();
                System.out.println("\f");
                hand1.print();
                System.out.println("\f");
                hand2.print();
                System.out.println("\f");
                hand3.print();
                System.out.println("\f");

                System.out.println("Cards left in the deck: " + deck.cards.size());
                deck.print();
                System.out.println("\f");
                System.out.println(hand1.hasThreeKind());*/

                int threeOfKindCounter = 0;
                int flushCounter = 0;
                int straightCounter = 0;
                int cycles = 2000;
                Deck deck;
                PokerHand hand;
                for (int i = 0; i <cycles ; i++) {
                        deck   = new Deck();
                        deck.shuffle();
                        hand = new PokerHand(cardsInHand);
                        hand.deal(deck, cardsInHand);
                    //    hand.print();
                        if(hand.hasFlush()) flushCounter++;
                        if(hand.hasThreeKind()) threeOfKindCounter++;
                        if(hand.hasStraight()) straightCounter++;
                }
                System.out.println("Flashes: " + flushCounter);
                System.out.println("Flash probability = " + 100*(double)flushCounter / cycles);
                System.out.println("Three of kind: " + threeOfKindCounter);
                System.out.println("Three of Kind probability = " + 100*(double)threeOfKindCounter / cycles);
                System.out.println("Straight: " + straightCounter);
                System.out.println("Straight probability = " + 100*(double)straightCounter / cycles);


        }

        public PokerHand(int nCards) {
                //Constructor in case on n cards in the hand
                super(nCards);

        }


        public void deal(Deck deck, int nCards){
                int startIndex = deck.cards.size() - nCards;
                int lastIndex = deck.cards.size()-1;
                Deck temp = deck.subdeck(startIndex, lastIndex);
                if(startIndex >=0) {
                        for (int i = 0; i < nCards; i++) {
                         this.cards.set(i, temp.cards.get(i));
                         deck.cards.remove(lastIndex-1-i);
                        }
                } else{System.out.println("Too few cards in the deck!");}

        }

        public boolean hasStraight(){
                PokerHand hand = this.handMergeSort();
                boolean flag = false;
                if(hand.cards.get(0).rank == hand.cards.get(1).rank-1 &&
                        hand.cards.get(1).rank == hand.cards.get(2).rank-1 &&
                        hand.cards.get(2).rank == hand.cards.get(3).rank-1 &&
                        hand.cards.get(3).rank == hand.cards.get(4).rank-1) {
                        flag = true;
                }
                if(!flag) return false;
                int[] suitArray = hand.histogramSuit();
                int twoSuitCardFlag = 0;
                for (int aSuitArray : suitArray){
                        if(aSuitArray >= 2) twoSuitCardFlag++;
                }
                return twoSuitCardFlag >= 2;
        }


        /**
         * @return hasFlush consists of two checks one by
         * one
         */
        public boolean hasFlush(){
                //Sorting of current hand
                PokerHand hand = this.handMergeSort();
                boolean flagSuit = false;
                /**Get histogram of card Suits*/
                int[] suitArray = hand.histogramSuit();

                for (int aSuitArray : suitArray) {
                        if (aSuitArray == 5) {
                                flagSuit = true;
                                break;
                        }
                }
                if(!flagSuit) return false;

                for (int i = 0; i < hand.cards.size() - 1 ; i++) {
                        if(hand.cards.get(i).rank == hand.cards.get(++i).rank-1){
                                return false;
                        }
                }
                return true;
        }

        private int[] histogramRank(){
                int [] temp = new int[Card.ranks.length];
                for (int i = 0; i <this.cards.size() ; i++) {
                        temp[this.cards.get(i).rank-1]++;
                }
                return temp;
}

        private int[] histogramSuit(){
                int [] temp = new int[Card.suits.length];
                for (int i = 0; i <this.cards.size() ; i++) {
                        temp[this.cards.get(i).suit]++;
                }
                return temp;
        }

        public boolean hasThreeKind (){
               //Flag for three cards of same rank
                int flag = -1;
                //Sorted hand where same rank goes one by one
                PokerHand hand = this.handMergeSort();
                int[] temp = new int[Card.ranks.length];

                for (int i = 0; i < hand.cards.size() - 2; i++) {
                        if(hand.cards.get(i).rank == hand.cards.get(i+1).rank &&
                                hand.cards.get(i).rank == hand.cards.get(i+2).rank) {
                                flag = i;
                        }
                }
                if(flag == -1){
                        return false;
                } else if(flag == 0) {
                        return hand.cards.get(flag + 3).suit != hand.cards.get(flag + 4).suit;
                } else if (flag == 1) {
                        return hand.cards.get(flag - 1).suit != hand.cards.get(flag + 3).suit;
                } else if(flag == 2){
                        return hand.cards.get(flag - 2).suit != hand.cards.get(flag - 1).suit;
                }
                return false;
        }

        /*
      * Makes a new deck of cards with a subset of cards from the original.
      * The old deck and new deck share references to the same card objects.
      */
        public PokerHand subdeck(int low, int high) {
                PokerHand sub = new PokerHand(high-low+1);
                for (int i = 0; i<high - low +1; i++) {
                        sub.cards.set(i, cards.get(low+i));
                }
                return sub;
        }


        public PokerHand handMergeSort() {
                if (cards.size() < 2) {
                        return this;
                }
                int mid = (cards.size()-1) / 2;

                // divide the deck roughly in half
                PokerHand d1 = subdeck(0, mid);
                PokerHand d2 = subdeck(mid+1, cards.size()-1);

                // sort the halves
                d1 = d1.handMergeSort();
                d2 = d2.handMergeSort();

                // merge the two halves and return the result
                // (d1 and d2 get garbage collected)
                return d1.handMerge(d2);
        }

        public PokerHand handMerge(PokerHand d1) {
                // create the new deck
                PokerHand result = new PokerHand (d1.cards.size() + this.cards.size());

                int choice;    // records the winner (1 means d1, 2 means d2)
                int i = 0;     // traverses the first input deck
                int j = 0;     // traverses the second input deck

                // k traverses the new (merged) deck
                for (int k = 0; k < result.cards.size(); k++) {
                        choice = 1;

                        // if d1 is empty, d2 wins; if d2 is empty, d1 wins; otherwise,
                        // compare the two cards
                        if (i == d1.cards.size())
                                choice = 2;
                        else if (j == this.cards.size())
                                choice = 1;
                        else if (d1.compareTo(i, this.cards.get(j)) > 0)
                                choice = 2;
                        else if (d1.compareTo(i, this.cards.get(j)) == 0)
                                choice = 0;
                        // make the new deck refer to the winner card
                        if (choice == 1) {
                                result.cards.set(k, d1.cards.get(i));  i++;
                        } else {
                                result.cards.set(k, this.cards.get(j));  j++;
                        }
                }
                return result;
        }


        /*
    * Compares two cards: returns 1 if the first card is greater
    * -1 if the seconds card is greater, and 0 if they are the equivalent.
    */
        public int compareTo(int i, Card that) {

                // use modulus arithmetic to rotate the ranks
                // so that the Ace is greater than the King.
                // WARNING: This only works with valid ranks!
                int rank1 = (this.cards.get(i).rank + 11) % 13;
                int rank2 = (that.rank + 11) % 13;

                // compare the rotated ranks

                if (rank1 > rank2) return 1;
                if (rank1 < rank2) return -1;
                return 0;
        }
}


