#gym is/was a library of environments developed by OpenAI purposed fortraining r-learning agents
import gym
import matplotlib.pyplot as plt
import numpy as np

# Michael Owen
# Import and initialize Mountain Car Environment
env = gym.make('MountainCar-v0')
env.reset()

def get_discrete_state(state,DISCRETE_OS_WIN_SIZE):
    discrete_state = (state-env.observation_space.low)/DISCRETE_OS_WIN_SIZE
    return tuple(discrete_state.astype(int))

def myFunc(e):
    return e[0]

# Define Q-learning function
def QLearning(env, learning, discount, epsilon, min_eps, episodes,bins):
    # Determine size of discretized state space
    #num_states = (env.observation_space.high - env.observation_space.low) * np.array([10, 100])
    #num_states = np.round(num_states, 0).astype(int) + 1
    DISCRETE_OS_SIZE = [bins] * len(env.observation_space.high)
    DISCRETE_OS_WIN_SIZE = (env.observation_space.high - env.observation_space.low) / DISCRETE_OS_SIZE

    # Initialize Q table
    #Q = np.random.uniform(low=-1, high=1,size=(num_states[0], num_states[1],env.action_space.n))
    Q = np.random.uniform(low=-1, high=1, size=(DISCRETE_OS_SIZE + [env.action_space.n]))

    # Initialize variables to track rewards
    reward_list = []
    ave_reward_list = []

    # Calculate episodic reduction in epsilon
    reduction = (epsilon - min_eps) / episodes
    #track number of successful episodes in last 100 episodes
    num_succeeded = 0
    #fartherest right horizontal position
    horizontal_pos_and_veloc = []
    max_h_pos = 0

    # Run Q learning algorithm
    for i in range(episodes):
        # Initialize parameters

        done = False
        tot_reward, reward = 0, 0
        state = env.reset()

        # Discretize state
        #state_adj = (state - env.observation_space.low) * np.array([10, 100])
        #state_adj = np.round(state_adj, 0).astype(int)

        state_adj = get_discrete_state(state,DISCRETE_OS_WIN_SIZE)

        while done != True:
            # Render environment for last five episodes
            if i >= (episodes - 20):
                env.render()

            # Determine next action - epsilon greedy strategy
            if np.random.random() < 1 - epsilon:
                action = np.argmax(Q[state_adj[0], state_adj[1]])
            else:
                action = np.random.randint(0, env.action_space.n)

            # Get next state and reward
            state2, reward, done, info = env.step(action)

            # Discretize state2
            #state2_adj = (state2 - env.observation_space.low) * np.array([10, 100])
            #state2_adj = np.round(state2_adj, 0).astype(int)
            state2_adj = get_discrete_state(state2,DISCRETE_OS_WIN_SIZE)

            # Allow for terminal states
            if done and state2[0] >= 0.5:
                num_succeeded +=1
                Q[state_adj[0], state_adj[1], action] = reward

            # Adjust Q value for current state
            else:
                delta = learning * (reward +
                                    discount * np.max(Q[state2_adj[0],
                                                        state2_adj[1]]) -
                                    Q[state_adj[0], state_adj[1], action])
                Q[state_adj[0], state_adj[1], action] += delta

            # Update variables
            tot_reward += reward
            state_adj = state2_adj

        # Decay epsilon
        if epsilon > min_eps:
            epsilon -= reduction

        # Track rewards
        reward_list.append(tot_reward)
        horizontal_pos_and_veloc.append(state2)
        if (i + 1) % 100 == 0:

            ave_reward = np.mean(reward_list)
            ave_reward_list.append(ave_reward)
            reward_list = []
            horizontal_pos_and_veloc.sort(key=myFunc, reverse=True)
            max_h_pos = horizontal_pos_and_veloc[0]
            horizontal_pos_and_veloc = []

        if (i + 1) % 100 == 0:
            print('Episode {} Average Reward: {}'.format(i + 1, ave_reward))
            print('{} episodes succeeded in the last 100 episodes'.format(num_succeeded))
            print('Max horizontal position: {}, Velocity at this position: {}\n'.format(max_h_pos[0],max_h_pos[1]))
            num_succeeded = 0

    env.close()

    return ave_reward_list


# Run Q-learning algorithm - Note - it's modified to take number of bins as last parameter
rewards = QLearning(env,0.1,1,0.1,0,5000, bins = 10)

#rewards_1 = QLearning(env,0.1,1,0.1,0,5000, bins = 5)
#rewards_2 = QLearning(env,0.1,1,0.1,0,5000, bins = 10)
#rewards_3 = QLearning(env,0.1,1,0.1,0,5000, bins = 20)
#rewards_4 = QLearning(env,0.1,1,0.1,0,5000, bins = 40)

#rewards_1 = QLearning(env, 0.2, 0.9, 0.6, 0.0, 5000, bins = 10)
#rewards_2 = QLearning(env, 0.3, 0.95, 0.5, 0.01, 5000, bins = 10)
#rewards_3 = QLearning(env, 0.15, 0.98, 0.4, 0.4, 5000, bins = 10)
#rewards_4 = QLearning(env, 0.1, 1, 0.1, 0, 5000, bins = 10)

#Note: best results obtained with rewards_4 parameter values

# Plot Rewards
plt.plot(100 * (np.arange(len(rewards)) + 1), rewards)
#plt.plot(100 * (np.arange(len(rewards_1)) + 1), rewards_1, label = "bins = 5")
#plt.plot(100 * (np.arange(len(rewards_2)) + 1), rewards_2, label = "bins = 10")
#plt.plot(100 * (np.arange(len(rewards_3)) + 1), rewards_3, label = "bins = 20")
#plt.plot(100 * (np.arange(len(rewards_4)) + 1), rewards_4, label = "bins = 40")
plt.xlabel('Episodes')
plt.ylabel('Average Reward')
plt.title('Average Reward vs Episodes')
#plt.legend()
plt.show()
plt.savefig('rewards.jpg')
plt.close()


