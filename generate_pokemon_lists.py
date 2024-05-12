#!/usr/bin/env python3
import requests
from pathlib import Path

POKEMON_LIST_JAVA_FILE = Path("src/main/java/com/peeteer/cobblemondetector/client/util/PokemonLists.java")

POKEMON_LIST_FILE_HEADER = """
// THIS IS A GENERATED FILE
// CONTAINS POKEMON-NAMES THAT MIGHT NOT BE IN COBBLEMON
//
// GENERATED USING https://pokeapi.co
//             SEE https://pokeapi.co/docs/graphql

package com.peeteer.cobblemondetector.client.util;

public class PokemonLists {
"""

POKEMON_LIST_LEGENDARY_HEADER = """
    // all legendaries
    public static final String[] legendaries = {
"""
POKEMON_LIST_LEGENDARY_FOOTER = """
    };
"""

POKEMON_LIST_MYTHIC_HEADER = """
    // apparently mythics and legendaries need to be separate
    public static final String[] mythics = {
"""
POKEMON_LIST_MYTHIC_FOOTER = """
    };
"""

POKEMON_LIST_STARTER_HEADER = """
    // all starter pokemon plus their evolutions
    public static final String[] starter = {
"""
POKEMON_LIST_STARTER_FOOTER = """
    };
"""

POKEMON_LIST_BABIES_HEADER = """
    // all baby pokemon without their evolutions
    public static final String[] babies = {
"""
POKEMON_LIST_BABIES_FOOTER = """
    };
"""

POKEMON_LIST_BEASTS_HEADER = """
    // all ultra-beasts
    public static final String[] ultra_beasts = {
"""
POKEMON_LIST_BEASTS_FOOTER = """
    };
"""

POKEMON_LIST_FILE_FOOTER = """
}
"""

graphql_query = """
query samplePokeAPIquery {
  legendaries: pokemon_v2_pokemonspecies(where: {is_legendary: {_eq: true}}) {
    name
  }
  mythics: pokemon_v2_pokemonspecies(where: {is_mythical: {_eq: true}}) {
    name
  }
  grass_starter: pokemon_v2_ability(where: {name: {_eq: "overgrow"}}) {
    name
    id
    pokemon_v2_pokemonabilities {
      pokemon_v2_pokemon {
        name
        pokemon_v2_pokemonabilities {
          is_hidden
          ability_id
        }
      }
    }
  }
  fire_starter: pokemon_v2_ability(where: {name: {_eq: "blaze"}}) {
    name
    id
    pokemon_v2_pokemonabilities {
      pokemon_v2_pokemon {
        name
        pokemon_v2_pokemonabilities {
          is_hidden
          ability_id
        }
      }
    }
  }
  water_starter: pokemon_v2_ability(where: {name: {_eq: "torrent"}}) {
    name
    id
    pokemon_v2_pokemonabilities {
      pokemon_v2_pokemon {
        name
        pokemon_v2_pokemonabilities {
          is_hidden
          ability_id
        }
      }
    }
  }
  babies: pokemon_v2_pokemonspecies(where: {is_baby: {_eq: true}}) {
    name
  }
  ultra_beasts: pokemon_v2_ability(where: {name: {_eq: "beast-boost"}}) {
    name
    id
    pokemon_v2_pokemonabilities {
      pokemon_v2_pokemon {
        name
      }
    }
  }
}
"""

graphql_headers = {
    "Content-Type": "application/json",
    "X-Method-Used": "graphiql"
}

response = requests.post("https://beta.pokeapi.co/graphql/v1beta", json={"query": graphql_query})
pokemondata = response.json()["data"]


legendaries = pokemondata["legendaries"]
legendaries_text = ""
for pokemon in legendaries:
    legendaries_text += f'        "{pokemon["name"]}",\n'

mythics = pokemondata["mythics"]
mythics_text = ""
for pokemon in mythics:
    mythics_text += f'        "{pokemon["name"]}",\n'

starter_abilities = pokemondata["grass_starter"] \
    + pokemondata["water_starter"] \
    + pokemondata["fire_starter"]
starters_text = ""
for ability in starter_abilities:
    # data complexity go brrr
    starter_ability_id = ability["id"]
    for pokemon in ability["pokemon_v2_pokemonabilities"]:
        pokemon = pokemon["pokemon_v2_pokemon"]

        is_starter = True
        for _ability in pokemon["pokemon_v2_pokemonabilities"]:
            # all starters of the same type have the same main ability (overgrow, torrent, blaze)
            # non-starters that also have such an ability have it as hidden-ability instead (6 total)
            if _ability["is_hidden"] and _ability["ability_id"] == starter_ability_id:
                is_starter = False
                break

        if is_starter:
            starters_text += f'        "{pokemon["name"]}",\n'
    pass

babies = pokemondata["babies"]
babies_text = ""
for pokemon in babies:
    babies_text += f'        "{pokemon["name"]}",\n'

beasts_text = ""
for ability in pokemondata["ultra_beasts"]:
    ability_id = ability["id"]
    for pokemon in ability["pokemon_v2_pokemonabilities"]:
        pokemon = pokemon["pokemon_v2_pokemon"]
        # all ultra-beasts have the same ability "beast boost"
        beasts_text += f'        "{pokemon["name"]}",\n'

# array-slicing is used to remove leading and trailing newlines
# [1:]  removes first char
# [:-1] removes last  char
pokemon_list_file_contents = ""
pokemon_list_file_contents += POKEMON_LIST_FILE_HEADER[1:]
pokemon_list_file_contents += POKEMON_LIST_LEGENDARY_HEADER
pokemon_list_file_contents += legendaries_text[:-1]
pokemon_list_file_contents += POKEMON_LIST_LEGENDARY_FOOTER
pokemon_list_file_contents += POKEMON_LIST_MYTHIC_HEADER
pokemon_list_file_contents += mythics_text[:-1]
pokemon_list_file_contents += POKEMON_LIST_MYTHIC_FOOTER
pokemon_list_file_contents += POKEMON_LIST_STARTER_HEADER
pokemon_list_file_contents += starters_text[:-1]
pokemon_list_file_contents += POKEMON_LIST_STARTER_FOOTER
pokemon_list_file_contents += POKEMON_LIST_BABIES_HEADER
pokemon_list_file_contents += babies_text[:-1]
pokemon_list_file_contents += POKEMON_LIST_BABIES_FOOTER
pokemon_list_file_contents += POKEMON_LIST_BEASTS_HEADER
pokemon_list_file_contents += beasts_text[:-1]
pokemon_list_file_contents += POKEMON_LIST_BEASTS_FOOTER
pokemon_list_file_contents += POKEMON_LIST_FILE_FOOTER[1:]

POKEMON_LIST_JAVA_FILE.write_text(pokemon_list_file_contents)
print(POKEMON_LIST_JAVA_FILE.read_text())
