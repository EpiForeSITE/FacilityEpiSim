---
title: 'FacilityEpiSim: an agent-based simulation package for infectious disease transmission in healthcare facilities'
tags:
  - Java
  - Repast
  - agent-based model
  - infectious disease transmission
  - healthcare
authors:
  - name: Damon J.A. Toth
    orcid: 0000-0001-7393-4814
    corresponding: true
    affiliation: "1, 2"
  - name: Willy Ray
    affiliation: "1, 2"
  - name: Om Sanghvi
    affiliation: "3"
  - name: Karim Khader
    orcid: 0000-0002-7206-8077
    affiliation: "1, 2"
affiliations:
 - name: University of Utah, United States
   index: 1
   ror: 03r0ha626
 - name: VA Salt Lake City Healthcare System, United States
   ror: 007fyq698
   index: 2
 - name: Hillcrest High School, Utah, United States
   index: 3
date: 12 December 2025
bibliography: paper.bib
---

# Summary

Mathematical modeling of healthcare-associated infection (HAI) epidemiology is a useful tool for 
addressing infections acquired during medical care, which cause significant morbidity, mortality,
and financial strain on health systems worldwide. HAIs arise from complex interactions among
patients, healthcare workers, the clinical environment, and microbial evolution. Because these
interconnected processes are difficult to observe in real-world or experimental settings, models 
provide a powerful, risk-free way to understand how infections spread and how interventions may
reduce transmission. We developed `FacilityEpiSim`, a continuous-time, agent-based simulation model of
infectious disease transmission in healthcare facilities. The software tool allows modelers and
their public health collaborators to run simulations to study transmission dynamics among facility
patients and evaluate the utility of patient surveillance strategies before implementing them.  

# Statement of need

The tool is an agent-based model (ABM) built with Repast Simphony 2.11.0 (@North2013RepastSimphony) for
simulating transmission of an infectious organism in a healthcare facility. The model simulates flow of
inpatients or residents of the facility over a specified time period, tracking patient admissions, lengths 
of stay, and discharges, disease importation and transmission dynamics, clinical detection and active 
surveillance testing with isolation of detected patients serving to partially decrease transmission. 
Patients in a susceptible state move to a colonized (i.e., infected) state at a rate proportional to the 
number of colonized patients currently in the facility, with detected colonized patients transmitting at a
discounted rate according to the assumed isolation effectiveness. Undetected colonized patients can
progress to clinical detection (i.e. a positive test driven by symptomatic, clinical infection) or can
spontaneously return to a susceptible state upon decolonization.

The main intervention that can be tested in the simulation is active surveillance, which can occur at
admission and/or at regular intervals during a patient's stay in the facility, with configurable
adherence rates, test sensitivity, and durations between mid-stay tests. Active surveillance can
identify asymptomatically colonized patients who would not otherwise have been detected and reduce
transmission according to a configurable isolation effectiveness parameter.

These features fill a need to have realistic representation of patient flow, disease transmission, and
interventions in a healthcare facility, so that researchers and public health strategists can disentangle
alternate explanations of epidemiological data and anticipate the potential effects of interventions to 
mitigate facility outbreaks. 

# State of the field

While several open source software packages exist for general infectious disease outbreak simulation
(e.g., @Jenness2013EpiModel, @Gozzi2025Epydemix, @Lorton2019CMS, @Grefenstette2013FRED, @Gallagher2024epiabm, 
@Meyer2023epiworldR), none of these provide settings specific to healthcare facility epidemiological scenarios
without significant customization efforts by the user. We found one public repository, H-outbreak, for
spatial-temporal simulation for hospital infection spread (@Kim2023HOutbreak), which emphasizes modeling
spatial hospital layout and staffing rather than transmission dynamics and surveillance.

We built `FacilityEpiSim` instead of extending existing projects because, to our knowledge, all the above 
simulation models use discrete time steps rather than the continuous-time, event-based framework implemented 
in our model. Using continuous time obviates the need for choosing a time step frequency that could 
unintentionally affect simulation dynamics and allows users to run a stochastic model that is directly
analogous to deterministic differential equation-based models that are commonly used in this field of
mathematical modeling research. 

# Software design

We designed `FacilityEpiSim` to implement our simulation tool using an object-oriented framework that
anticipates potential future extensions to multiple facilities and multiple diseases circulating
simultaneously among the patient population. Multiple objects of classes `Person`, `PersonDisease`, 
`Facility` and `FacilityOutbreak` and can be used to simulate and study outbreaks in an agent-based
simulation framework. The timing of events was designed using the built-in event simulation capabilities
of Repast. We designed customizable simulation settings to match anticipated experimental use cases and
also built R scripts designed to analyze and display output for this purpose. 

# Research impact statement

A non open-source version of our code was used to generate results for three prior research publications: 
@Slayton2015VitalSigns, @Toth2017LTACHCREnetwork, and @Toth2020CDiffVaccine. These studies demonstrated 
the utility of this simulation model for generating novel insights for public health. With this open source
version of our model, users can now configure simulation settings to particular facilities, infectious 
organisms, and surveillance intervention strategies of interest (\autoref{fig:parampanel}).

![Parameters panel available when running the non-batch version of the simulation in Repast Simphony.\label{fig:parampanel}](./docs/vignettes/images/parameters-panel.JPG){ width=40% }

The code generates time series outputs, event logs for admissions, transmissions, detections, etc., and can
also perform batch runs with parameter sweeps to test a range of assumptions or surveillance strategies. We also
provide R scripts that analyze raw simulation output to verify model behaviors and view sensitivity analysis
results. These capabilities will allow users to extend our prior, published research findings and generate new
insights for public health.

# AI Usage Disclosure

Generative AI was used in the creation of this project. Tools used include GitHub Copilot, Copilot Agents, and
Claude Code. A variety of models were used with each tool, including Sonnet 4.5, Opus 4.5, GPT 4, GPT 5 and 
GPT-5 mini. These were used primarily in the creation of code and documentation. AI assisted in the generation 
of code, debugging, bootstrapping data analysis, scaffolding for documentation and some documentation generation. 
The authors of this paper assert that human authors reviewed, edited and validated all AI-assisted outputs and 
made the core design decisions.

# Acknowledgements

This work was supported by the Centers for Disease Control and Prevention, Modeling Infectious Diseases in
Healthcare Network award U01CK000585 and Insight Net award number CDC-RFA-FT-23-0069.

# References
