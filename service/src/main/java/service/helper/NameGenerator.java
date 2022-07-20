package service.helper;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This name generator class produces random names based on one
 * adjective and an animal name
 */
@Component
public class NameGenerator {
    // Source: https://argoprep.com/blog/206-personality-adjectives-to-describe-anybody/
    private static List<String> ADJECTIVES = Arrays.asList(
            "Adaptable", "Courageous", "Giving", "Neat", "Self-confident",
            "Adventurous", "Creative", "Good", "Nice", "Self-disciplined",
            "Affable", "Decisive", "Gregarious", "Non-judgemental", "Sensible",
            "Affectionate", "Dependable", "Hardworking", "Observant", "Sensitive",
            "Agreeable", "Determined", "Helpful", "Optimistic", "Shy",
            "Ambitious", "Diligent", "Hilarious", "Organized", "Silly",
            "Amiable", "Diplomatic", "Honest", "Passionate", "Sincere",
            "Amicable", "Discreet", "Humorous", "Patient", "Smart",
            "Amusing", "Dynamic", "Imaginative", "Persistent", "Socialable",
            "Artistic", "Easy-going", "Impartial", "Pioneering", "Straight-Forward",
            "Brave", "Emotional", "Independent", "Philosophical", "Sympathetic",
            "Bright", "Efficient", "Industrious", "Placid", "Talkative",
            "Broad-minded", "Energetic", "Intelligent", "Plucky", "Thoughtful",
            "Calm", "Enthusiastic", "Intellectual", "Polite", "Tidy",
            "Careful", "Extroverted", "Intuitive", "Popular", "Tough",
            "Charismatic", "Exuberant", "Inventive", "Powerful", "Trustworthy",
            "Charming", "Fair-minded", "Joyful", "Practical", "Unassuming",
            "Chatty", "Faithful", "Kind", "Pro-active", "Understanding",
            "Cheerful", "Fearless", "Kooky", "Quick-witted", "Upbeat",
            "Clever", "Forceful", "Laid-back", "Quiet", "Versatile",
            "Communicative", "Frank", "Likable", "Rational", "Warmhearted",
            "Compassionate", "Friendly", "Loving", "Reliable", "Wild",
            "Conscientious", "Funny", "Loyal", "Reserved", "Wise",
            "Considerate", "Generous", "Lucky", "Resourceful", "Witty",
            "Convivial", "Gentle", "Modest", "Romantic"
    );

    // Source: https://www.aplustopper.com/list-of-animals/
    private static List<String> ANIMALS = Arrays.asList(
            "Cow", "Rabbit", "Duck", "Pig", "Bee", "Goat", "Crab",
            "Fish", "Chicken", "Horse", "Dog", "Llama", "Ostrich",
            "Camel", "Shrimp", "Deer", "Turkey", "Dove", "Sheep",
            "Cat", "Goose", "Ox", "Reindeer", "Antelope", "Eagle",
            "Porcupine", "Bat", "Bear", "Axolotl", "Camel", "Chimpanzee",
            "Coyote", "Deer", "Gorilla", "Hare", "Hedgehog", "Hippopotamus",
            "Kangaroo", "Badger", "Elephant", "Elk", "Fox", "Giraffe",
            "Koala", "Leopard", "Lion", "Lizard", "Monkey", "Otter",
            "Owl", "Mole", "Panda", "Rabbit", "Rhinoceros", "Raccoon",
            "Rat", "Squirrel", "Tiger", "Walrus", "Bison", "Reindeer",
            "Possum", "Chipmunk", "Porcupine", "Wolf", "Woodpecker",
            "Zebra", "Wombat", "Capybara", "Dodo"
    );

    public String generateName() {
        Random random = new Random();
        String randomAdjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size() - 1));
        String randomAnimal = ANIMALS.get(random.nextInt(ANIMALS.size() - 1));

        return randomAdjective + "_" + randomAnimal;
    }
}
